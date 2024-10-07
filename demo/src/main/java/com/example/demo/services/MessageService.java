package com.example.demo.services;

import com.example.demo.dto.MessageCreateDTO;
import com.example.demo.dto.MessageResponseDTO;
import com.example.demo.exceptions.MessageNotFoundException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.Conversation;
import com.example.demo.models.Message;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.ConversationRepository;
import com.example.demo.repositorys.MessageRepository;
import com.example.demo.repositorys.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ConversationRepository conversationRepository;

    public MessageService(MessageRepository messageRepository, UtilisateurRepository utilisateurRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.conversationRepository = conversationRepository;
    }

    private Conversation createOrGetConversation(Utilisateur fromUser, Utilisateur toUser) {
        Conversation conversation = conversationRepository.findByVendeurAndAcheteur(fromUser, toUser)
                .orElse(conversationRepository.findByVendeurAndAcheteur(toUser, fromUser)
                        .orElse(null));

        if (conversation == null) {
            conversation = new Conversation();
            conversation.setAcheteur(fromUser);
            conversation.setVendeur(toUser);
            conversation = conversationRepository.save(conversation);
        }

        return conversation;
    }
    @Transactional
    public MessageResponseDTO sendMessage(MessageCreateDTO messageCreateDTO, Utilisateur fromUser) {
        Utilisateur toUser = utilisateurRepository.findById(messageCreateDTO.getToUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Conversation conversation = createOrGetConversation(fromUser, toUser);

        Message message = new Message();
        message.setFromUser(fromUser);
        message.setToUser(toUser);
        message.setContent(messageCreateDTO.getContent());
        message.setConversation(conversation);

        Message savedMessage = messageRepository.save(message);
        return convertToResponseDTO(savedMessage);
    }
    @Transactional
    public MessageResponseDTO replyToMessage(MessageCreateDTO messageCreateDTO, Long parentMessageId, Utilisateur fromUser) {
        Message parentMessage = messageRepository.findById(parentMessageId)
                .orElseThrow(() -> new MessageNotFoundException("Message not found"));

        Utilisateur toUser = parentMessage.getFromUser();

        Message replyMessage = new Message();
        replyMessage.setFromUser(fromUser);
        replyMessage.setToUser(toUser);
        replyMessage.setContent(messageCreateDTO.getContent());
        replyMessage.setParentMessage(parentMessage);

        Message savedReplyMessage = messageRepository.save(replyMessage);
        return convertToResponseDTO(savedReplyMessage);
    }

    public List<MessageResponseDTO> getSentMessages(Long userId) {
        List<Message> sentMessages = messageRepository.findByFromUserId(userId);
        return sentMessages.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MessageResponseDTO> getReceivedMessages(Long userId) {
        List<Message> receivedMessages = messageRepository.findByToUserId(userId);
        return receivedMessages.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private MessageResponseDTO convertToResponseDTO(Message message) {
        return new MessageResponseDTO(
                message.getId(),
                message.getContent(),
                message.getTimestamp(),
                message.getFromUser().getId(),
                message.getToUser().getId(),
                message.getFromUser().getUsername(),
                message.getToUser().getUsername(),
                message.getParentMessage() != null ? message.getParentMessage().getId() : null
        );
    }
}
