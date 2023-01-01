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

import ch.ethz.ssh2.Connection
import ch.ethz.ssh2.SCPClient
import com.google.common.collect.Maps
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * 远程执行linux命令
 * @author: ZhangHouYing
 * @date: 2019-08-10 10:06
 */
class ScpClientUtil(
    private val ip: String,
    private val port: Int,
    private val username: String,
    private val password: String
) {
    fun getFile(remoteFile: String?, localTargetDirectory: String?) {
        val conn = Connection(ip, port)
        try {
            conn.connect()
            val isAuthenticated = conn.authenticateWithPassword(username, password)
            if (!isAuthenticated) {
                System.err.println("authentication failed")
            }
            val client = SCPClient(conn)
            client[remoteFile]
        } catch (ex: IOException) {
            Logger.getLogger(SCPClient::class.java.name).log(Level.SEVERE, null, ex)
        } finally {
            conn.close()
        }
    }

    fun putFile(localFile: String?, remoteTargetDirectory: String?) {
        putFile(localFile, null, remoteTargetDirectory)
    }

    @JvmOverloads
    fun putFile(localFile: String?, remoteFileName: String?, remoteTargetDirectory: String?, mode: String? = null) {
        var mode = mode
        val conn = Connection(ip, port)
        try {
            conn.connect()
            val isAuthenticated = conn.authenticateWithPassword(username, password)
            if (!isAuthenticated) {
                System.err.println("authentication failed")
            }
            val client = SCPClient(conn)
            if (mode == null || mode.length == 0) {
                mode = "0600"
            }
            //todo 改api
            if (remoteFileName == null) {
                client.put(localFile, 0, remoteTargetDirectory, "")
            } else {
                client.put(localFile, remoteFileName.toLong(), remoteTargetDirectory, mode)
            }
        } catch (ex: IOException) {
            Logger.getLogger(ScpClientUtil::class.java.name).log(Level.SEVERE, null, ex)
        } finally {
            conn.close()
        }
    }

    companion object {
        private val instance: MutableMap<String, ScpClientUtil?> = Maps.newHashMap()
        @Synchronized
        fun getInstance(ip: String, port: Int, username: String, password: String): ScpClientUtil? {
            if (instance[ip] == null) {
                instance[ip] = ScpClientUtil(ip, port, username, password)
            }
            return instance[ip]
        }
    }
}