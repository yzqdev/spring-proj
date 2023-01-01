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
package me.zhengjie.modules.mnt.util

import cn.hutool.core.io.IoUtil
import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import lombok.extern.slf4j.Slf4j
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.*

/**
 * 执行shell命令
 *
 * @author: ZhangHouYing
 * @date: 2019/8/10
 */
@Slf4j
class ExecuteShellUtil(ipAddress: String?, username: String?, password: String?, port: Int) {
    private var stdout: Vector<String>? = null
    var session: Session? = null

    init {
        try {
            val jsch = JSch()
            session = jsch.getSession(username, ipAddress, port)
            session.setPassword(password)
            session.setConfig("StrictHostKeyChecking", "no")
            session.connect(3000)
        } catch (e: Exception) {
            ExecuteShellUtil.log.error(e.message, e)
        }
    }

    fun execute(command: String?): Int {
        val returnCode = 0
        var channel: ChannelShell? = null
        var printWriter: PrintWriter? = null
        var input: BufferedReader? = null
        stdout = Vector()
        try {
            channel = session!!.openChannel("shell") as ChannelShell
            channel!!.connect()
            input = BufferedReader(InputStreamReader(channel.inputStream))
            printWriter = PrintWriter(channel.outputStream)
            printWriter.println(command)
            printWriter.println("exit")
            printWriter.flush()
            ExecuteShellUtil.log.info("The remote command is: ")
            var line: String
            while (input.readLine().also { line = it } != null) {
                stdout!!.add(line)
            }
        } catch (e: Exception) {
            ExecuteShellUtil.log.error(e.message, e)
            return -1
        } finally {
            IoUtil.close(printWriter)
            IoUtil.close(input)
            channel?.disconnect()
        }
        return returnCode
    }

    fun close() {
        if (session != null) {
            session!!.disconnect()
        }
    }

    fun executeForResult(command: String?): String {
        execute(command)
        val sb = StringBuilder()
        for (str in stdout!!) {
            sb.append(str)
        }
        return sb.toString()
    }
}