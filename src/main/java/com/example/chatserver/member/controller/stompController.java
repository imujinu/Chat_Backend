package com.example.chatserver.member.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class stompController {
    @MessageMapping("/{roomId}") // 클라이언트에서 특정 pulblish/roomId로 메시지 발행시 이 경로로 매핑된다.
    @SendTo("/topic/{roomId}") // 해당 roomId에 메세지를 발행하여 구독중인 클라이언트에게 메시지 전송
    //@DestinationVariable 은 MessageMapping으로 정의된 Controller내에서만 사용된다.
    public String sendMessage(@DestinationVariable Long roomId, String message){
        System.out.println("roomid+::" + roomId+ message);
        return message;
    }
}
