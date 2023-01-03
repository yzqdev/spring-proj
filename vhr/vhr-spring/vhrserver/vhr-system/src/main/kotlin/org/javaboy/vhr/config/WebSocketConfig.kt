package org.javaboy.vhr.config

org.springframework.messaging.simp.config.MessageBrokerRegistry
@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws/ep")
            .setAllowedOrigins("http://localhost:8080")
            .withSockJS()
    }

    fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/queue")
    }
}