package com.okestro.symphony.dashboard.cmm.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        //구독 topic 등록시 앞에 붙이는 prefix
        config.enableSimpleBroker("/topic","/queue");
        // websocket 메세지 전달시 앞에 붙이는 prefix
        config.setApplicationDestinationPrefixes("/openstack/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 소켓 연결 주소
        registry.addEndpoint("/websocket/connect").setAllowedOrigins("*").withSockJS();
    }
}
