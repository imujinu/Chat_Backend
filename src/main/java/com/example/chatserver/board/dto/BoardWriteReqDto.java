package com.example.chatserver.board.dto;

import com.example.chatserver.board.domain.Board;
import com.example.chatserver.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardWriteReqDto {
    private String title;
    private String contents;
    private long authorId;

    public Board toEntity(Member member){
        return Board.builder()
                .title(this.title)
                .contents(this.contents)
                .member(member)
                .build();
    }
}
