package com.example.demo.services;

import com.example.demo.models.Message;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.MessageRepository;
import com.example.demo.repositorys.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Message sendMessage(Utilisateur fromUser, Long toUserId, String content) {
        Utilisateur toUser = utilisateurRepository.findById(toUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Message message = new Message();
        message.setFromUser(fromUser);
        message.setToUser(toUser);
        message.setContent(content);

        return messageRepository.save(message);
    }

    public Message replyToMessage(Utilisateur fromUser, Long parentMessageId, String content) {
        Message parentMessage = messageRepository.findById(parentMessageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));

        Utilisateur toUser = parentMessage.getFromUser();

        Message replyMessage = new Message();
        replyMessage.setFromUser(fromUser);
        replyMessage.setToUser(toUser);
        replyMessage.setContent(content);
        replyMessage.setParentMessage(parentMessage);

        return messageRepository.save(replyMessage);
    }

    public List<Message> getSentMessages(Long userId) {
        return messageRepository.findByFromUserId(userId);
    }

    public List<Message> getReceivedMessages(Long userId) {
        return messageRepository.findByToUserId(userId);
    }
}
