package com.example.chatserver.member.controller;

import com.example.chatserver.member.dto.ChatMeesageReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class stompController {

    private final SimpMessageSendingOperations messageTemplates;
    // 방법 1. MessageMapping(수신) 과 SenTo ( topic에 메시지전달) 한꺼번에 처리
//    @MessageMapping("/{roomId}") // 클라이언트에서 특정 pulblish/roomId로 메시지 발행시 이 경로로 매핑된다.
//    @SendTo("/topic/{roomId}") // 해당 roomId에 메세지를 발행하여 구독중인 클라이언트에게 메시지 전송
//    //@DestinationVariable 은 MessageMapping으로 정의된 Controller내에서만 사용된다.
//    public String sendMessage(@DestinationVariable Long roomId, String message){
//        System.out.println("roomid+::" + roomId+ message);
//        return message;
//    }

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMeesageReqDto dto){
        System.out.println("roomid+::" + roomId+ dto.getMessage());
        messageTemplates.convertAndSend("/topic/"+roomId, dto);
    }
}
