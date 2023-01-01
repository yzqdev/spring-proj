/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.mnt.service.impl

import cn.hutool.core.date.DatePattern
import cn.hutool.core.date.DateUtil
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.mnt.domain.Deploy
import me.zhengjie.modules.mnt.domain.DeployHistory
import me.zhengjie.modules.mnt.repository.DeployRepository
import me.zhengjie.modules.mnt.service.DeployHistoryService
import me.zhengjie.modules.mnt.service.DeployService
import me.zhengjie.modules.mnt.service.ServerDeployService
import me.zhengjie.modules.mnt.service.dto.AppDto
import me.zhengjie.modules.mnt.service.dto.DeployDto
import me.zhengjie.modules.mnt.service.dto.DeployQueryCriteria
import me.zhengjie.modules.mnt.service.mapstruct.DeployMapper
import me.zhengjie.modules.mnt.util.ExecuteShellUtil
import me.zhengjie.modules.mnt.util.ScpClientUtil
import me.zhengjie.modules.mnt.websocket.MsgType
import me.zhengjie.modules.mnt.websocket.SocketMsg
import me.zhengjie.modules.mnt.websocket.WebSocketServer
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.utils.SecurityUtils
import me.zhengjie.utils.ValidationUtil.isNull
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
class DeployServiceImpl : DeployService {
    private val FILE_SEPARATOR = "/"
    private val deployRepository: DeployRepository? = null
    private val deployMapper: DeployMapper? = null
    private val serverDeployService: ServerDeployService? = null
    private val deployHistoryService: DeployHistoryService? = null

    /**
     * 循环次数
     */
    private val count = 30
    override fun queryAll(criteria: DeployQueryCriteria?, pageable: Pageable?): Any {
        val page = deployRepository.findAll(
            Specification { root: Root<Deploy>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }, pageable
        )
        return toPage(page.map { entity: Deploy -> deployMapper!!.toDto(entity) })
    }

    override fun queryAll(criteria: DeployQueryCriteria?): List<DeployDto?>? {
        return deployMapper.toDto(deployRepository.findAll(Specification { root: Root<Deploy>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        }))
    }

    override fun findById(id: Long): DeployDto? {
        val deploy = deployRepository!!.findById(id).orElseGet { Deploy() }!!
        isNull(deploy.id, "Deploy", "id", id)
        return deployMapper!!.toDto(deploy)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: Deploy) {
        deployRepository!!.save(resources)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: Deploy) {
        val deploy = deployRepository!!.findById(resources.id).orElseGet { Deploy() }!!
        isNull(deploy.id, "Deploy", "id", resources.id)
        deploy.copy(resources)
        deployRepository.save(deploy)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(ids: Set<Long>) {
        for (id in ids) {
            deployRepository!!.deleteById(id)
        }
    }

    override fun deploy(fileSavePath: String, id: Long) {
        deployApp(fileSavePath, id)
    }

    /**
     * @param fileSavePath 本机路径
     * @param id ID
     */
    private fun deployApp(fileSavePath: String, id: Long) {
        val deploy = findById(id)
        if (deploy == null) {
            sendMsg("部署信息不存在", MsgType.ERROR)
            throw BadRequestException("部署信息不存在")
        }
        val app = deploy.app
        if (app == null) {
            sendMsg("包对应应用信息不存在", MsgType.ERROR)
            throw BadRequestException("包对应应用信息不存在")
        }
        val port = app.port
        //这个是服务器部署路径
        val uploadPath = app.uploadPath
        val sb = StringBuilder()
        var msg: String
        val deploys = deploy.deploys
        for (deployDTO in deploys) {
            val ip = deployDTO.ip
            val executeShellUtil = getExecuteShellUtil(ip)
            //判断是否第一次部署
            val flag = checkFile(executeShellUtil, app)
            //第一步要确认服务器上有这个目录
            executeShellUtil.execute("mkdir -p " + app.uploadPath)
            executeShellUtil.execute("mkdir -p " + app.backupPath)
            executeShellUtil.execute("mkdir -p " + app.deployPath)
            //上传文件
            msg = String.format("登陆到服务器:%s", ip)
            val scpClientUtil = getScpClientUtil(ip)
            DeployServiceImpl.log.info(msg)
            sendMsg(msg, MsgType.INFO)
            msg = String.format("上传文件到服务器:%s<br>目录:%s下，请稍等...", ip, uploadPath)
            sendMsg(msg, MsgType.INFO)
            scpClientUtil!!.putFile(fileSavePath, uploadPath)
            if (flag) {
                sendMsg("停止原来应用", MsgType.INFO)
                //停止应用
                stopApp(port, executeShellUtil)
                sendMsg("备份原来应用", MsgType.INFO)
                //备份应用
                backupApp(
                    executeShellUtil,
                    ip,
                    app.deployPath + FILE_SEPARATOR,
                    app.name,
                    app.backupPath + FILE_SEPARATOR,
                    id
                )
            }
            sendMsg("部署应用", MsgType.INFO)
            //部署文件,并启动应用
            val deployScript = app.deployScript
            executeShellUtil.execute(deployScript)
            sleep(3)
            sendMsg("应用部署中，请耐心等待部署结果，或者稍后手动查看部署状态", MsgType.INFO)
            var i = 0
            var result = false
            // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
            while (i++ < count) {
                result = checkIsRunningStatus(port, executeShellUtil)
                if (result) {
                    break
                }
                // 休眠6秒
                sleep(6)
            }
            sb.append("服务器:").append(deployDTO.name).append("<br>应用:").append(app.name)
            sendResultMsg(result, sb)
            executeShellUtil.close()
        }
    }

    private fun sleep(second: Int) {
        try {
            Thread.sleep((second * 1000).toLong())
        } catch (e: InterruptedException) {
            DeployServiceImpl.log.error(e.message, e)
        }
    }

    private fun backupApp(
        executeShellUtil: ExecuteShellUtil,
        ip: String,
        fileSavePath: String,
        appName: String,
        backupPath: String,
        id: Long
    ) {
        var backupPath: String? = backupPath
        val deployDate = DateUtil.format(Date(), DatePattern.PURE_DATETIME_PATTERN)
        val sb = StringBuilder()
        backupPath += """
            $appName$FILE_SEPARATOR$deployDate
            
            """.trimIndent()
        sb.append("mkdir -p ").append(backupPath)
        sb.append("mv -f ").append(fileSavePath)
        sb.append(appName).append(" ").append(backupPath)
        DeployServiceImpl.log.info("备份应用脚本:$sb")
        executeShellUtil.execute(sb.toString())
        //还原信息入库
        val deployHistory = DeployHistory()
        deployHistory.appName = appName
        deployHistory.deployUser = SecurityUtils.getCurrentUsername()
        deployHistory.ip = ip
        deployHistory.deployId = id
        deployHistoryService!!.create(deployHistory)
    }

    /**
     * 停App
     *
     * @param port 端口
     * @param executeShellUtil /
     */
    private fun stopApp(port: Int, executeShellUtil: ExecuteShellUtil) {
        //发送停止命令
        executeShellUtil.execute(String.format("lsof -i :%d|grep -v \"PID\"|awk '{print \"kill -9\",$2}'|sh", port))
    }

    /**
     * 指定端口程序是否在运行
     *
     * @param port 端口
     * @param executeShellUtil /
     * @return true 正在运行  false 已经停止
     */
    private fun checkIsRunningStatus(port: Int, executeShellUtil: ExecuteShellUtil): Boolean {
        val result = executeShellUtil.executeForResult(String.format("fuser -n tcp %d", port))
        return result!!.indexOf("/tcp:") > 0
    }

    private fun sendMsg(msg: String, msgType: MsgType) {
        try {
            WebSocketServer.Companion.sendInfo(SocketMsg(msg, msgType), "deploy")
        } catch (e: IOException) {
            DeployServiceImpl.log.error(e.message, e)
        }
    }

    override fun serverStatus(resources: Deploy): String {
        val serverDeploys = resources.deploys
        val app = resources.app
        for (serverDeploy in serverDeploys) {
            val sb = StringBuilder()
            val executeShellUtil = getExecuteShellUtil(serverDeploy.ip)
            sb.append("服务器:").append(serverDeploy.name).append("<br>应用:").append(app.name)
            val result = checkIsRunningStatus(app.port, executeShellUtil)
            if (result) {
                sb.append("<br>正在运行")
                sendMsg(sb.toString(), MsgType.INFO)
            } else {
                sb.append("<br>已停止!")
                sendMsg(sb.toString(), MsgType.ERROR)
            }
            DeployServiceImpl.log.info(sb.toString())
            executeShellUtil.close()
        }
        return "执行完毕"
    }

    private fun checkFile(executeShellUtil: ExecuteShellUtil, appDTO: AppDto): Boolean {
        val result = executeShellUtil.executeForResult("find " + appDTO.deployPath + " -name " + appDTO.name)
        return result.indexOf(appDTO.name) > 0
    }

    /**
     * 启动服务
     * @param resources /
     * @return /
     */
    override fun startServer(resources: Deploy): String {
        val deploys = resources.deploys
        val app = resources.app
        for (deploy in deploys) {
            val sb = StringBuilder()
            val executeShellUtil = getExecuteShellUtil(deploy.ip)
            //为了防止重复启动，这里先停止应用
            stopApp(app.port, executeShellUtil)
            sb.append("服务器:").append(deploy.name).append("<br>应用:").append(app.name)
            sendMsg("下发启动命令", MsgType.INFO)
            executeShellUtil.execute(app.startScript)
            sleep(3)
            sendMsg("应用启动中，请耐心等待启动结果，或者稍后手动查看运行状态", MsgType.INFO)
            var i = 0
            var result = false
            // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
            while (i++ < count) {
                result = checkIsRunningStatus(app.port, executeShellUtil)
                if (result) {
                    break
                }
                // 休眠6秒
                sleep(6)
            }
            sendResultMsg(result, sb)
            DeployServiceImpl.log.info(sb.toString())
            executeShellUtil.close()
        }
        return "执行完毕"
    }

    /**
     * 停止服务
     * @param resources /
     * @return /
     */
    override fun stopServer(resources: Deploy): String {
        val deploys = resources.deploys
        val app = resources.app
        for (deploy in deploys) {
            val sb = StringBuilder()
            val executeShellUtil = getExecuteShellUtil(deploy.ip)
            sb.append("服务器:").append(deploy.name).append("<br>应用:").append(app.name)
            sendMsg("下发停止命令", MsgType.INFO)
            //停止应用
            stopApp(app.port, executeShellUtil)
            sleep(1)
            val result = checkIsRunningStatus(app.port, executeShellUtil)
            if (result) {
                sb.append("<br>关闭失败!")
                sendMsg(sb.toString(), MsgType.ERROR)
            } else {
                sb.append("<br>关闭成功!")
                sendMsg(sb.toString(), MsgType.INFO)
            }
            DeployServiceImpl.log.info(sb.toString())
            executeShellUtil.close()
        }
        return "执行完毕"
    }

    override fun serverReduction(resources: DeployHistory): String {
        val deployId = resources.deployId
        val deployInfo = deployRepository!!.findById(deployId).orElseGet { Deploy() }!!
        val deployDate = DateUtil.format(resources.deployDate, DatePattern.PURE_DATETIME_PATTERN)
        val app = deployInfo.app
        if (app == null) {
            sendMsg("应用信息不存在：" + resources.appName, MsgType.ERROR)
            throw BadRequestException("应用信息不存在：" + resources.appName)
        }
        var backupPath = app.backupPath + FILE_SEPARATOR
        backupPath += resources.appName + FILE_SEPARATOR + deployDate
        //这个是服务器部署路径
        val deployPath = app.deployPath
        val ip = resources.ip
        val executeShellUtil = getExecuteShellUtil(ip)
        val msg: String
        msg = String.format("登陆到服务器:%s", ip)
        DeployServiceImpl.log.info(msg)
        sendMsg(msg, MsgType.INFO)
        sendMsg("停止原来应用", MsgType.INFO)
        //停止应用
        stopApp(app.port, executeShellUtil)
        //删除原来应用
        sendMsg("删除应用", MsgType.INFO)
        executeShellUtil.execute("rm -rf " + deployPath + FILE_SEPARATOR + resources.appName)
        //还原应用
        sendMsg("还原应用", MsgType.INFO)
        executeShellUtil.execute("cp -r $backupPath/. $deployPath")
        sendMsg("启动应用", MsgType.INFO)
        executeShellUtil.execute(app.startScript)
        sendMsg("应用启动中，请耐心等待启动结果，或者稍后手动查看启动状态", MsgType.INFO)
        var i = 0
        var result = false
        // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
        while (i++ < count) {
            result = checkIsRunningStatus(app.port, executeShellUtil)
            if (result) {
                break
            }
            // 休眠6秒
            sleep(6)
        }
        val sb = StringBuilder()
        sb.append("服务器:").append(ip).append("<br>应用:").append(resources.appName)
        sendResultMsg(result, sb)
        executeShellUtil.close()
        return ""
    }

    private fun getExecuteShellUtil(ip: String): ExecuteShellUtil {
        val serverDeployDTO = serverDeployService!!.findByIp(ip)
        if (serverDeployDTO == null) {
            sendMsg("IP对应服务器信息不存在：$ip", MsgType.ERROR)
            throw BadRequestException("IP对应服务器信息不存在：$ip")
        }
        return ExecuteShellUtil(ip, serverDeployDTO.account, serverDeployDTO.password, serverDeployDTO.port)
    }

    private fun getScpClientUtil(ip: String): ScpClientUtil? {
        val serverDeployDTO = serverDeployService!!.findByIp(ip)
        if (serverDeployDTO == null) {
            sendMsg("IP对应服务器信息不存在：$ip", MsgType.ERROR)
            throw BadRequestException("IP对应服务器信息不存在：$ip")
        }
        return ScpClientUtil.Companion.getInstance(
            ip,
            serverDeployDTO.port,
            serverDeployDTO.account,
            serverDeployDTO.password
        )
    }

    private fun sendResultMsg(result: Boolean, sb: StringBuilder) {
        if (result) {
            sb.append("<br>启动成功!")
            sendMsg(sb.toString(), MsgType.INFO)
        } else {
            sb.append("<br>启动失败!")
            sendMsg(sb.toString(), MsgType.ERROR)
        }
    }

    @Throws(IOException::class)
    override fun download(queryAll: List<DeployDto?>?, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (deployDto in queryAll!!) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["应用名称"] = deployDto.getApp().name
            map["服务器"] = deployDto.getServers()
            map["部署日期"] = deployDto.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }
}