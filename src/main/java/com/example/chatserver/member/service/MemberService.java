package com.example.chatserver.member.service;

import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberLoginReqDto;
import com.example.chatserver.member.dto.MemberSaveReqDto;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public Member save(MemberSaveReqDto memberSaveReqDto){
        memberRepository.findByEmail(memberSaveReqDto.getEmail()).ifPresent(a->new IllegalArgumentException("이미 존재하는 이메일 입니다."));

        return memberRepository.save(Member.builder().
                name(memberSaveReqDto.getName())
                .email(memberSaveReqDto.getEmail())
                .password(passwordEncoder.encode(memberSaveReqDto.getPassword()))
                        .build());
    }

    public Member login(MemberLoginReqDto memberLoginReqDto){
       Member member =  memberRepository.findByEmail(memberLoginReqDto.getEmail()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 유저입니다."));
       if(!passwordEncoder.matches(memberLoginReqDto.getPassword(), memberLoginReqDto.getPassword())){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
       }
       return member;
    }

}
