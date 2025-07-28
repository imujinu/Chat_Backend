package com.example.chatserver.member.service;

import com.example.chatserver.member.domain.*;
import com.example.chatserver.member.dto.ChatMessageDto;
import com.example.chatserver.member.dto.ChatRoomListResDto;
import com.example.chatserver.member.dto.MyChatListResDto;
import com.example.chatserver.member.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MemberRepository memberRepository;


    public void saveMessage(Long roomId, ChatMessageDto dto) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        // 보낸 사람 조회
        Member member = memberRepository.findByEmail(dto.getSenderEmail()).orElseThrow();

        // 메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .content(dto.getMessage())
                .member(member)
                .build();
                chatMessageRepository.save(chatMessage);

                // 사용자 별로 읽음 여부 저장

        List<ChatParticipant> participantList = chatParticipantRepository.findByChatRoom(chatRoom);
        for(ChatParticipant p : participantList){
            ReadStatus readStatus = ReadStatus.builder()
                    .chatMessage(chatMessage)
                    .chatRoom(chatRoom)
                    .member(member)
                    .isRead(p.getMember().equals(member))
                    .build();

            readStatusRepository.save(readStatus);
        }

    }

    public void createGroupRoom(String roomName) {

        // 방 생성자는 곧장 채팅방에 참가해야 한다.
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new NoSuchElementException());
        // 채팅방을 만들어준다.

        ChatRoom chatRoom = ChatRoom.builder()
                .name(roomName)
                .isGroupChat("Y")
                .build();

        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatRoom.getParticipantList().add(chatParticipant);

        chatRoomRepository.save(chatRoom);


    }

    public List<ChatRoomListResDto> getGroupchatRooms() {
        return chatRoomRepository.findByIsGroupChat("Y").stream().map(a-> new ChatRoomListResDto().fromEntity(a)).collect(Collectors.toList());
    }

    public void addParticipantToGroupChat(Long roomId) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new EntityNotFoundException(""));
        // 멤버 조회
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new EntityNotFoundException(""));
        // 이미 참여자인지 검증
        Optional<ChatParticipant> participant = chatParticipantRepository.findByChatRoomAndMember(chatRoom,member);

        // ChatParticipant 객체 생성 후 저장

        if(!participant.isPresent()){


        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();

        chatParticipantRepository.save(chatParticipant);

        }
    }


    public List<ChatMessageDto> getChatHistory(Long roomId) {
        // 내가 해당 채팅방의 참여자가 아닐 경우 에러
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new EntityNotFoundException(""));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new EntityNotFoundException(""));
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        boolean check = false;
        for(ChatParticipant c : chatParticipants){
            if(c.getMember().equals(member)){
                check=true;
            }
        }
        if(!check)throw new IllegalStateException("본인이 속하지 않은 채팅방입니다.");
        // 특정 room 에대한 message
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        for(ChatMessage c : chatMessages){
            ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                    .message(c.getContent())
                    .senderEmail(member.getEmail())
                    .build();
            chatMessageDtos.add(chatMessageDto);
        }
        return chatMessageDtos;
    }

    public boolean isRoomParticipant(String email, Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new EntityNotFoundException(""));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new EntityNotFoundException(""));

        return chatParticipantRepository.findByChatRoom(chatRoom).stream().anyMatch(p -> p.getMember().equals(member));

    }

    public void messageRead(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new EntityNotFoundException(""));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new EntityNotFoundException(""));
        List<ReadStatus> readStatuses = readStatusRepository.findByChatRoomAndMember(chatRoom,member);

        for(ReadStatus r : readStatuses){
            r.updateIsRead(true);
        }


    }

    public List<MyChatListResDto> getMyChatRoom() {
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new NoSuchElementException());
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByMember(member);
        List<MyChatListResDto> dtos = new ArrayList<>();
        for(ChatParticipant c : chatParticipants){
            ChatRoom chatRoom = c.getChatRoom();
            Long count = readStatusRepository.findByChatRoomAndMemberAndIsReadFalse(member,chatRoom);
            MyChatListResDto resDto = MyChatListResDto.builder()
                    .roomId(chatRoom.getId())
                    .roomName(chatRoom.getName())
                    .isGroupChat(chatRoom.getIsGroupChat())
                    .ueReadCount(count)
                    .build();
            dtos.add(resDto);
        }
        return dtos;
    }

    public void leaveGroupChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new EntityNotFoundException(""));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new NoSuchElementException());

        if(chatRoom.getIsGroupChat().equals("N")){
            throw new IllegalArgumentException("단채채팅방이 아닙니다.");
        }

        ChatParticipant c = chatParticipantRepository.findByChatRoomAndMember(chatRoom,member).orElseThrow(()->new EntityNotFoundException("참여자를 찾을 수 없습니다."));
        chatParticipantRepository.delete(c);

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);

        if(chatParticipants.isEmpty()){
            chatRoomRepository.delete(chatRoom);
        }

    }

    public Long getOrCreatePrivateRoom(Long otherMemberId) {

        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new NoSuchElementException());
        Member otherMember = memberRepository.findById(otherMemberId).orElseThrow(()->new EntityNotFoundException());
        member.deleteMember("Y");
    }
}
