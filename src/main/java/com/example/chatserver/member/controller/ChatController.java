package com.example.chatserver.member.controller;

import com.example.chatserver.member.dto.ChatMessageDto;
import com.example.chatserver.member.dto.ChatRoomListResDto;
import com.example.chatserver.member.dto.MyChatListResDto;
import com.example.chatserver.member.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/room/group/create")
    public ResponseEntity<?> createGroupRoom(@RequestParam String roomName){
        chatService.createGroupRoom(roomName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/group/list")
    public ResponseEntity<?> getGroupChatRooms(){
        List<ChatRoomListResDto> chatRooms = chatService.getGroupchatRooms();
        return new ResponseEntity<>(chatRooms, HttpStatus.OK);
    }

    @PostMapping("/room/group/{roomId}/join")
    public ResponseEntity<?> joinGroupChatRoom(@PathVariable Long roomId){
        chatService.addParticipantToGroupChat(roomId);
        return ResponseEntity.ok().build();
    }

<<<<<<< HEAD
    @GetMapping("/history/{roomId}")
    public ResponseEntity<?> getChatHistory(@PathVariable Long roomId){
        List<ChatMessageDto> chatMessageDtos = chatService.getChatHistory(roomId);
        return new ResponseEntity<>(chatMessageDtos, HttpStatus.OK);
=======
    @PostMapping("/room/{roomId}/read")
    public ResponseEntity<?> messageRead(@PathVariable Long roomId){
        chatService.messageRead(roomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my/rooms")
    public ResponseEntity<?> getMyChatRooms(){
        List<MyChatListResDto> myChatListResDtos = chatService.getMyChatRoom();
        return new ResponseEntity<>(myChatListResDtos, HttpStatus.OK);
>>>>>>> 9a1785a (chat)
    }
}
