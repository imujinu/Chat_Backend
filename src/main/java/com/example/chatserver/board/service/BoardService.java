package com.example.chatserver.board.service;

import com.example.chatserver.board.domain.Board;
import com.example.chatserver.board.dto.BoardListResDto;
import com.example.chatserver.board.dto.BoardWriteReqDto;
import com.example.chatserver.board.repository.BoardRepository;
import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    public Board write(BoardWriteReqDto boardWriteReqDto){
        Member member = memberRepository.findById(boardWriteReqDto.getAuthorId()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 유저 입니다."));

        return boardRepository.save(boardWriteReqDto.toEntity(member));
    }

    public Page<BoardListResDto> list(Pageable pageable){
        Page<Board> boardList = boardRepository.findAll(pageable);
        return boardList.map((board -> new BoardListResDto().fromEntity(board)));
    }
}
