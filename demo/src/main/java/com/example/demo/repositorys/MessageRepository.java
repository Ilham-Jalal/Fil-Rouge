package com.example.demo.repositorys;

import com.example.demo.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByFromUserId(Long fromUserId);
    List<Message> findByToUserId(Long toUserId);
    List<Message> findByConversationId(Long conversationId);

}
