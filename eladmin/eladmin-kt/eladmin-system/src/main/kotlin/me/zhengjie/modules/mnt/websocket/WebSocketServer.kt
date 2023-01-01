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
package me.zhengjie.modules.mnt.websocket

import com.alibaba.fastjson.JSONObject
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint

/**
 * @author ZhangHouYing
 * @date 2019-08-10 15:46
 */
@ServerEndpoint("/webSocket/{sid}")
@Slf4j
@Component
class WebSocketServer {
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private var session: Session? = null

    /**
     * 接收sid
     */
    private var sid = ""

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    fun onOpen(session: Session?, @PathParam("sid") sid: String) {
        this.session = session
        //如果存在就先删除一个，防止重复推送消息
        for (webSocket in webSocketSet) {
            if (webSocket.sid == sid) {
                webSocketSet.remove(webSocket)
            }
        }
        webSocketSet.add(this)
        this.sid = sid
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    fun onClose() {
        webSocketSet.remove(this)
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    fun onMessage(message: String, session: Session?) {
        WebSocketServer.log.info("收到来" + sid + "的信息:" + message)
        //群发消息
        for (item in webSocketSet) {
            try {
                item.sendMessage(message)
            } catch (e: IOException) {
                WebSocketServer.log.error(e.message, e)
            }
        }
    }

    @OnError
    fun onError(session: Session?, error: Throwable) {
        WebSocketServer.log.error("发生错误")
        error.printStackTrace()
    }

    /**
     * 实现服务器主动推送
     */
    @Throws(IOException::class)
    private fun sendMessage(message: String) {
        session!!.basicRemote.sendText(message)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as WebSocketServer
        return session == that.session && sid == that.sid
    }

    override fun hashCode(): Int {
        return Objects.hash(session, sid)
    }

    companion object {
        /**
         * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
         */
        private val webSocketSet = CopyOnWriteArraySet<WebSocketServer>()

        /**
         * 群发自定义消息
         */
        @Throws(IOException::class)
        fun sendInfo(socketMsg: SocketMsg?, @PathParam("sid") sid: String?) {
            val message = JSONObject.toJSONString(socketMsg)
            WebSocketServer.log.info("推送消息到$sid，推送内容:$message")
            for (item in webSocketSet) {
                try {
                    //这里可以设定只推送给这个sid的，为null则全部推送
                    if (sid == null) {
                        item.sendMessage(message)
                    } else if (item.sid == sid) {
                        item.sendMessage(message)
                    }
                } catch (ignored: IOException) {
                }
            }
        }
    }
}