package com.example.chatserver.member.repository;

import com.example.chatserver.member.domain.ChatRoom;
import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
    List<ReadStatus> findByChatRoomAndMember(ChatRoom chatRoom, Member member);


    Long findByChatRoomAndMemberAndIsReadFalse(Member member, ChatRoom chatRoom);
}
