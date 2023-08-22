package com.socialmediaapi.repository;

import com.socialmediaapi.dto.MessageDTO;
import com.socialmediaapi.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> getMessageById(Long messageId);

    @Query("SELECT new com.socialmediaapi.dto.MessageDTO(m.id, m.sender.id, m.receiver.id, m.messageContent) " +
            "FROM Message m WHERE m.sender.id = :userId OR m.receiver.id = :userId1")
    List<MessageDTO> findBySenderIdOrReceiverId(Long userId, Long userId1);
}
