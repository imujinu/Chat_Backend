package com.example.chatserver.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOrigins("http://localhost:3000")
                //ws:// 가 아닌 http:// 엔드포인트로 사용
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //publish/1 형태로 발행해야 함을 설정
        // publish 시작하는 url 패턴으로 메시지가 발행되면 @Controller 객체의 @messageMapping 메서드로 라우팅
        registry.setApplicationDestinationPrefixes("/publish");
//        topic/1 형태로 메시지를 수신 해야 함을 설정
        registry.enableSimpleBroker("/topic");
    }

    // 웹소켓 요청 (connect, subscribe, disconnect) 등의 요청시에는 http header 등 http 메시지를 넣어올 수 있고 ,
    // 이를 interceptor를 통해 가로 토큰 등을 검증
    // 토큰 유효성 검증
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
