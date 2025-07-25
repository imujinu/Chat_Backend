package com.example.chatserver.member.repository;

import com.example.chatserver.member.domain.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant,Long> {
}
