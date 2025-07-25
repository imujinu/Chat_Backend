package com.example.chatserver.member.dto;

import com.example.chatserver.member.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomListResDto {
    private Long id;
    private String name;

    public ChatRoomListResDto fromEntity(ChatRoom chatRoom){
        return ChatRoomListResDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .build();
    }
}
