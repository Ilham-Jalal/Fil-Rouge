package com.example.demo.services;

import com.example.demo.dto.ConversationResponseDTO;
import com.example.demo.dto.MessageResponseDTO;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.Conversation;
import com.example.demo.models.Message;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.ConversationRepository;
import com.example.demo.repositorys.MessageRepository;
import com.example.demo.repositorys.UtilisateurRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository;

    public ConversationService(ConversationRepository conversationRepository,
                               MessageRepository messageRepository,
                               UtilisateurRepository utilisateurRepository
                               ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<MessageResponseDTO> getMessagesByConversationId(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        return messages.stream()
                .map(message -> {
                    MessageResponseDTO dto = new MessageResponseDTO();
                    dto.setId(message.getId());
                    dto.setContent(message.getContent());
                    dto.setTimestamp(message.getTimestamp());
                    dto.setFromUserName(message.getFromUser() != null ? message.getFromUser().getUsername() : "Utilisateur inconnu");
                    dto.setToUserName(message.getToUser() != null ? message.getToUser().getUsername() : "Utilisateur inconnu");
                    return dto;
                })
                .toList();
    }

    @Transactional
    public Conversation createOrGetConversation(Long vendeurId, Long acheteurId) {
        Conversation conversation = conversationRepository.findByVendeurIdAndAcheteur_Id(vendeurId, acheteurId)
                .orElse(conversationRepository.findByVendeurIdAndAcheteur_Id(acheteurId, vendeurId)
                        .orElse(null));

        if (conversation == null) {
            Utilisateur vendeur = utilisateurRepository.findById(vendeurId)
                    .orElseThrow(() -> new UserNotFoundException("Vendeur not found"));
            Utilisateur acheteur = utilisateurRepository.findById(acheteurId)
                    .orElseThrow(() -> new UserNotFoundException("Acheteur not found"));

            conversation = new Conversation();
            conversation.setVendeur(vendeur);
            conversation.setAcheteur(acheteur);
            conversation = conversationRepository.save(conversation);
        }

        return conversation;
    }

    @Transactional
    public Conversation createConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    public List<ConversationResponseDTO> getConversationsByUserId(Long userId) {
        return conversationRepository.findAllByUserId(userId).stream()
                .map(conversation -> new ConversationResponseDTO(
                        conversation.getId(),
                        conversation.getAcheteur() != null ? conversation.getAcheteur().getUsername() : null,
                        conversation.getVendeur() != null ? conversation.getVendeur().getUsername() : null,
                        conversation.getLastMessage() != null ? conversation.getLastMessage().getContent() : null,
                        conversation.getLastMessage() != null ? conversation.getLastMessage().getTimestamp() : null
                ))
                .toList();
    }

    public List<ConversationResponseDTO> getConversationsByUsername(String username) {
        Optional<Utilisateur> user = utilisateurRepository.findByUsername(username);
        if (user.isEmpty()) {
            return Collections.emptyList();
        }
        return getConversationsByUserId(user.get().getId());
    }

    @Transactional(readOnly = true)
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Conversation getConversationById(Long id) {
        return conversationRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteConversation(Long id) {
        conversationRepository.deleteById(id);
    }

    @Transactional
    public Message addMessageToConversation(Long conversationId, Message message) {
        Conversation conversation = getConversationById(conversationId);

        if (conversation != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();

            Optional<Utilisateur> optionalCurrentUser = utilisateurRepository.findByUsername(currentUserName);

            if (optionalCurrentUser.isPresent()) {
                Utilisateur currentUser = optionalCurrentUser.get();

                message.setConversation(conversation);
                message.setFromUser(currentUser);
                message.setToUser(conversation.getAcheteur());

                return messageRepository.save(message);
            } else {
                throw new IllegalArgumentException("Utilisateur non trouvé");
            }
        }
        return null;
    }
}
