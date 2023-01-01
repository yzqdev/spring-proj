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
package me.zhengjie.modules.system.service.implimport

import cn.hutool.core.date.BetweenFormatter
import cn.hutool.core.date.DateUtil
import me.zhengjie.modules.system.service.MonitorService
import me.zhengjie.utils.ElAdminConstant
import me.zhengjie.utils.FileUtil.getSize
import me.zhengjie.utils.StringUtils.localIp
import org.springframework.stereotype.Service
import oshi.SystemInfo
import oshi.hardware.CentralProcessor
import oshi.hardware.GlobalMemory
import oshi.software.os.OperatingSystem
import oshi.util.FormatUtil
import oshi.util.Util
import java.lang.management.ManagementFactory
import java.text.DecimalFormat
import java.util.*

me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.base.BaseMapper.toEntity
import me.zhengjie.base.BaseEntity.toString
import me.zhengjie.utils.RsaUtils.decryptByPrivateKey
import me.zhengjie.utils.enums.CodeBiEnum.Companion.find
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.base.BaseMapper.toDto
import me.zhengjie.utils.ValidationUtil.isNull
import me.zhengjie.utils.RedisUtils.delByKeys
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.enums.DataScopeEnum.Companion.find
import me.zhengjie.utils.QueryHelp.getAllFields
import me.zhengjie.utils.RedisUtils.del
import me.zhengjie.modules.security.service.UserCacheClean.cleanUserCache
import me.zhengjie.modules.security.service.OnlineUserService.kickOutForUsername
import me.zhengjie.utils.FileUtil.checkSize
import me.zhengjie.utils.FileUtil.getExtensionName
import me.zhengjie.utils.FileUtil.upload
import me.zhengjie.config.FileProperties.path
import me.zhengjie.utils.RedisUtils.get
import me.zhengjie.utils.RedisUtils.set
import me.zhengjie.utils.FileUtil.getSize
import me.zhengjie.utils.StringUtils.localIp
import org.springframework.web.bind.annotation.RestController
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.RequestMapping
import me.zhengjie.modules.system.service.JobService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.security.access.prepost.PreAuthorize
import javax.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.validation.annotation.Validated
import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.system.rest.JobController
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import me.zhengjie.modules.system.service.DeptService
import me.zhengjie.modules.system.service.dto.DeptQueryCriteria
import me.zhengjie.modules.system.service.dto.DeptDto
import me.zhengjie.modules.system.domain.Dept
import me.zhengjie.modules.system.rest.DeptController
import cn.hutool.core.collection.CollectionUtil
import me.zhengjie.modules.system.service.DictService
import me.zhengjie.modules.system.service.dto.DictQueryCriteria
import me.zhengjie.modules.system.rest.DictController
import me.zhengjie.modules.system.service.MenuService
import me.zhengjie.modules.system.service.mapstruct.MenuMapper
import me.zhengjie.modules.system.service.dto.MenuQueryCriteria
import me.zhengjie.modules.system.service.dto.MenuDto
import org.springframework.web.bind.annotation.RequestParam
import me.zhengjie.modules.system.rest.MenuController
import me.zhengjie.modules.system.service.RoleService
import org.springframework.web.bind.annotation.PathVariable
import me.zhengjie.modules.system.service.dto.RoleQueryCriteria
import me.zhengjie.modules.system.rest.RoleController
import me.zhengjie.modules.system.service.dto.RoleDto
import me.zhengjie.modules.system.service.dto.RoleSmallDto
import org.springframework.security.crypto.password.PasswordEncoder
import me.zhengjie.modules.system.service.UserService
import me.zhengjie.modules.system.service.DataService
import me.zhengjie.modules.system.service.VerifyService
import me.zhengjie.modules.system.service.dto.UserQueryCriteria
import me.zhengjie.modules.system.domain.vo.UserPassVo
import me.zhengjie.utils.RsaUtils
import me.zhengjie.config.RsaProperties
import me.zhengjie.modules.system.service.dto.UserDto
import org.springframework.web.multipart.MultipartFile
import me.zhengjie.utils.enums.CodeEnum
import me.zhengjie.annotation.rest.AnonymousGetMapping
import me.zhengjie.modules.system.rest.LimitController
import me.zhengjie.domain.vo.EmailVo
import me.zhengjie.utils.enums.CodeBiEnum
import me.zhengjie.modules.system.service.MonitorService
import me.zhengjie.modules.system.service.DictDetailService
import me.zhengjie.modules.system.service.dto.DictDetailQueryCriteria
import org.springframework.data.web.PageableDefault
import me.zhengjie.modules.system.service.dto.DictDetailDto
import me.zhengjie.modules.system.domain.DictDetail
import me.zhengjie.modules.system.rest.DictDetailController
import me.zhengjie.modules.system.domain.vo.MenuMetaVo
import me.zhengjie.modules.system.domain.vo.MenuVo
import lombok.AllArgsConstructor
import me.zhengjie.base.BaseEntity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import com.alibaba.fastjson.annotation.JSONField
import javax.persistence.ManyToMany
import javax.persistence.JoinTable
import javax.persistence.JoinColumn
import me.zhengjie.utils.enums.DataScopeEnum
import javax.persistence.FetchType
import lombok.NoArgsConstructor
import me.zhengjie.base.BaseDTO
import me.zhengjie.modules.system.service.dto.JobSmallDto
import me.zhengjie.modules.system.service.dto.DeptSmallDto
import me.zhengjie.modules.system.service.dto.DictSmallDto
import me.zhengjie.annotation.DataPermission
import org.springframework.cache.annotation.CacheConfig
import me.zhengjie.modules.system.repository.JobRepository
import me.zhengjie.modules.system.service.mapstruct.JobMapper
import me.zhengjie.utils.RedisUtils
import me.zhengjie.modules.system.repository.UserRepository
import javax.persistence.criteria.CriteriaBuilder
import me.zhengjie.utils.QueryHelp
import me.zhengjie.modules.system.service.dto.JobDto
import me.zhengjie.exception.EntityExistException
import org.springframework.cache.annotation.CacheEvict
import me.zhengjie.modules.system.repository.DeptRepository
import me.zhengjie.modules.system.service.mapstruct.DeptMapper
import me.zhengjie.modules.system.repository.RoleRepository
import me.zhengjie.modules.system.repository.DictRepository
import me.zhengjie.modules.system.service.mapstruct.DictMapper
import me.zhengjie.modules.system.service.dto.DictDto
import me.zhengjie.modules.system.repository.MenuRepository
import me.zhengjie.modules.system.service.mapstruct.RoleMapper
import me.zhengjie.modules.system.service.mapstruct.RoleSmallMapper
import me.zhengjie.modules.security.service.UserCacheClean
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import me.zhengjie.modules.system.service.mapstruct.UserMapper
import me.zhengjie.config.FileProperties
import me.zhengjie.modules.security.service.OnlineUserService
import cn.hutool.extra.template.TemplateUtil
import cn.hutool.extra.template.TemplateConfig
import cn.hutool.core.util.RandomUtil
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OSFileStore
import me.zhengjie.utils.ElAdminConstant
import oshi.hardware.GlobalMemory
import oshi.hardware.VirtualMemory
import oshi.hardware.CentralProcessor
import cn.hutool.core.date.BetweenFormatter
import me.zhengjie.modules.system.repository.DictDetailRepository
import me.zhengjie.modules.system.service.mapstruct.DictDetailMapper
import org.mapstruct.ReportingPolicy
import me.zhengjie.base.BaseMapper
import me.zhengjie.modules.system.service.mapstruct.DictSmallMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying

/**
 * @author Zheng Jie
 * @date 2020-05-02
 */
@Service
class MonitorServiceImpl : MonitorService {
    private val df = DecimalFormat("0.00")
    override fun getServers(): Map<String, Any> {
        val resultMap: MutableMap<String, Any> = LinkedHashMap(8)
        try {
            val si = SystemInfo()
            val os = si.operatingSystem
            val hal = si.hardware
            // 系统信息
            resultMap["sys"] = getSystemInfo(os)
            // cpu 信息
            resultMap["cpu"] = getCpuInfo(hal.processor)
            // 内存信息
            resultMap["memory"] = getMemoryInfo(hal.memory)
            // 交换区信息
            resultMap["swap"] = getSwapInfo(hal.memory)
            // 磁盘
            resultMap["disk"] = getDiskInfo(os)
            resultMap["time"] = DateUtil.format(Date(), "HH:mm:ss")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultMap
    }

    /**
     * 获取磁盘信息
     * @return /
     */
    private fun getDiskInfo(os: OperatingSystem): Map<String, Any> {
        val diskInfo: MutableMap<String, Any> = LinkedHashMap()
        val fileSystem = os.fileSystem
        val fsArray = fileSystem.fileStores
        val osName = System.getProperty("os.name")
        var available: Long = 0
        var total: Long = 0
        for (fs in fsArray) {
            // windows 需要将所有磁盘分区累加，linux 和 mac 直接累加会出现磁盘重复的问题，待修复
            if (osName.lowercase(Locale.getDefault()).startsWith(ElAdminConstant.WIN)) {
                available += fs.usableSpace
                total += fs.totalSpace
            } else {
                available = fs.usableSpace
                total = fs.totalSpace
                break
            }
        }
        val used = total - available
        diskInfo["total"] = if (total > 0) getSize(total) else "?"
        diskInfo["available"] = getSize(available)
        diskInfo["used"] = getSize(used)
        if (total != 0L) {
            diskInfo["usageRate"] = df.format(used / total.toDouble() * 100)
        } else {
            diskInfo["usageRate"] = 0
        }
        return diskInfo
    }

    /**
     * 获取交换区信息
     * @param memory /
     * @return /
     */
    private fun getSwapInfo(memory: GlobalMemory): Map<String, Any> {
        val swapInfo: MutableMap<String, Any> = LinkedHashMap()
        val virtualMemory = memory.virtualMemory
        val total = virtualMemory.swapTotal
        val used = virtualMemory.swapUsed
        swapInfo["total"] = FormatUtil.formatBytes(total)
        swapInfo["used"] = FormatUtil.formatBytes(used)
        swapInfo["available"] = FormatUtil.formatBytes(total - used)
        if (used == 0L) {
            swapInfo["usageRate"] = 0
        } else {
            swapInfo["usageRate"] = df.format(used / total.toDouble() * 100)
        }
        return swapInfo
    }

    /**
     * 获取内存信息
     * @param memory /
     * @return /
     */
    private fun getMemoryInfo(memory: GlobalMemory): Map<String, Any> {
        val memoryInfo: MutableMap<String, Any> = LinkedHashMap()
        memoryInfo["total"] = FormatUtil.formatBytes(memory.total)
        memoryInfo["available"] = FormatUtil.formatBytes(memory.available)
        memoryInfo["used"] = FormatUtil.formatBytes(memory.total - memory.available)
        memoryInfo["usageRate"] = df.format((memory.total - memory.available) / memory.total.toDouble() * 100)
        return memoryInfo
    }

    /**
     * 获取Cpu相关信息
     * @param processor /
     * @return /
     */
    private fun getCpuInfo(processor: CentralProcessor): Map<String, Any> {
        val cpuInfo: MutableMap<String, Any> = LinkedHashMap()
        cpuInfo["name"] = processor.processorIdentifier.name
        cpuInfo["package"] = processor.physicalPackageCount.toString() + "个物理CPU"
        cpuInfo["core"] = processor.physicalProcessorCount.toString() + "个物理核心"
        cpuInfo["coreNumber"] = processor.physicalProcessorCount
        cpuInfo["logic"] = processor.logicalProcessorCount.toString() + "个逻辑CPU"
        // CPU信息
        val prevTicks = processor.systemCpuLoadTicks
        // 等待1秒...
        Util.sleep(1000)
        val ticks = processor.systemCpuLoadTicks
        val user = ticks[CentralProcessor.TickType.USER.index] - prevTicks[CentralProcessor.TickType.USER.index]
        val nice = ticks[CentralProcessor.TickType.NICE.index] - prevTicks[CentralProcessor.TickType.NICE.index]
        val sys = ticks[CentralProcessor.TickType.SYSTEM.index] - prevTicks[CentralProcessor.TickType.SYSTEM.index]
        val idle = ticks[CentralProcessor.TickType.IDLE.index] - prevTicks[CentralProcessor.TickType.IDLE.index]
        val iowait = ticks[CentralProcessor.TickType.IOWAIT.index] - prevTicks[CentralProcessor.TickType.IOWAIT.index]
        val irq = ticks[CentralProcessor.TickType.IRQ.index] - prevTicks[CentralProcessor.TickType.IRQ.index]
        val softirq =
            ticks[CentralProcessor.TickType.SOFTIRQ.index] - prevTicks[CentralProcessor.TickType.SOFTIRQ.index]
        val steal = ticks[CentralProcessor.TickType.STEAL.index] - prevTicks[CentralProcessor.TickType.STEAL.index]
        val totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal
        cpuInfo["used"] = df.format(100.0 * user / totalCpu + 100.0 * sys / totalCpu)
        cpuInfo["idle"] = df.format(100.0 * idle / totalCpu)
        return cpuInfo
    }

    /**
     * 获取系统相关信息,系统、运行天数、系统IP
     * @param os /
     * @return /
     */
    private fun getSystemInfo(os: OperatingSystem): Map<String, Any> {
        val systemInfo: MutableMap<String, Any> = LinkedHashMap()
        // jvm 运行时间
        val time = ManagementFactory.getRuntimeMXBean().startTime
        val date = Date(time)
        // 计算项目运行时间
        val formatBetween = DateUtil.formatBetween(date, Date(), BetweenFormatter.Level.HOUR)
        // 系统信息
        systemInfo["os"] = os.toString()
        systemInfo["day"] = formatBetween
        systemInfo["ip"] = localIp
        return systemInfo
    }
}