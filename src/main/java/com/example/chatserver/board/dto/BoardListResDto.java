package com.example.chatserver.board.dto;

import com.example.chatserver.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListResDto {
    private Long id;
    private String title;
    private String memberEmail;

    public BoardListResDto fromEntity(Board board){
        return BoardListResDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .memberEmail(board.getMember().getEmail())
                .build();
    }
}
