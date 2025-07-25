package com.example.chatserver.member.repository;

import com.example.chatserver.member.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository  extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByName(String roomName);

    List<ChatRoom> findByIsGroupChat(String isGroup);
}
