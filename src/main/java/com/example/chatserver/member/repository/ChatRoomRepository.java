package com.example.chatserver.member.repository;

import com.example.chatserver.member.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository  extends JpaRepository<ChatRoom, Long> {

}
