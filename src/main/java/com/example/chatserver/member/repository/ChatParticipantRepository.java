package com.example.chatserver.member.repository;

import com.example.chatserver.member.domain.ChatParticipant;
import com.example.chatserver.member.domain.ChatRoom;
import com.example.chatserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant,Long> {

    List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);

    Optional<ChatParticipant> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

    List<ChatParticipant> findAllByMember(Member member);
}
