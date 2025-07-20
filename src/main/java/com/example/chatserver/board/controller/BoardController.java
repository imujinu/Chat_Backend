package com.example.chatserver.board.controller;

import com.example.chatserver.board.domain.Board;
import com.example.chatserver.board.dto.BoardListResDto;
import com.example.chatserver.board.dto.BoardWriteReqDto;
import com.example.chatserver.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/write")
    public ResponseEntity<?> write(@RequestBody BoardWriteReqDto boardWriteReqDto){
        return new ResponseEntity<>(boardService.write(boardWriteReqDto).getId()+"번 글 작성 완료", HttpStatus.CREATED);

    }

    @PostMapping("/list")
    public ResponseEntity<?> list(@PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        Page<BoardListResDto> boardPage = boardService.list(pageable);
        return new ResponseEntity<>(boardPage,HttpStatus.ACCEPTED);
    }
}
