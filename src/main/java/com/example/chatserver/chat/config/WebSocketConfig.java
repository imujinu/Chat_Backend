package com.example.chatserver.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final SpringWebSocketHandler springWebSocketHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // /connect url로 websocket연결 요청이 들어오면, 핸들러 클래스가 처리
        registry.addHandler(springWebSocketHandler, "/connect")
                //securityconfig에서의 cors 예외는 http 요청에 대한 예외,
                // 따라서 websocket 프로토콜에 대한 요청에 대해서는 별도의 cors 요청이 필요
                .setAllowedOrigins("http://localhost:3000");
    }
}
