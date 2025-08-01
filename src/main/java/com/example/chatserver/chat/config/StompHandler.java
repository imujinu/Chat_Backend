package com.example.chatserver.chat.config;

import com.example.chatserver.member.service.ChatService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final ChatService chatService;

    @Value("${jwt.secretKey}")
    private String secretKey;

    // stomp의 요청이 들어오면 우선 preSend가 intercept해서 유효성을 검증한다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT == accessor.getCommand()){
            System.out.println("connect 요청 시 토큰 유효성 검증");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("토큰 검증 완료");
        }

        if(StompCommand.SUBSCRIBE == accessor.getCommand()){

            System.out.println("구독중인지 검증");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String email = claims.getSubject();
            String roomId = accessor.getDestination().split("/")[2];

            if(!chatService.isRoomParticipant(email,Long.parseLong(roomId))){
                throw new AuthenticationServiceException("해당 채팅방에 권한이 없습니다.");
            }
          }
        return message;
    }
}
