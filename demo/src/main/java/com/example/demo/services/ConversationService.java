package com.example.demo.services;

import com.example.demo.dto.MessageResponseDTO;
import com.example.demo.models.Conversation;
import com.example.demo.models.Message;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.ConversationRepository;
import com.example.demo.repositorys.MessageRepository;
import com.example.demo.repositorys.UtilisateurRepository; // Importez le repository Utilisateur
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository; // Ajoutez ce champ

    public ConversationService(ConversationRepository conversationRepository,
                               MessageRepository messageRepository,
                               UtilisateurRepository utilisateurRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.utilisateurRepository = utilisateurRepository; // Initialisez le repository Utilisateur
    }

    public List<MessageResponseDTO> getMessagesByConversationId(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        return messages.stream().map(message -> {
            MessageResponseDTO dto = new MessageResponseDTO();
            dto.setId(message.getId());
            dto.setContent(message.getContent());
            dto.setTimestamp(message.getTimestamp());

            // Gestion des utilisateurs null
            dto.setFromUserName(message.getFromUser() != null ? message.getFromUser().getUsername() : "Utilisateur inconnu");
            dto.setToUserName(message.getToUser() != null ? message.getToUser().getUsername() : "Utilisateur inconnu");

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Conversation createConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
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
        // Récupérer la conversation par ID
        Conversation conversation = getConversationById(conversationId);

        if (conversation != null) {
            // Obtenir l'utilisateur authentifié
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();

            // Rechercher l'utilisateur à partir de son nom d'utilisateur
            Optional<Utilisateur> optionalCurrentUser = utilisateurRepository.findByUsername(currentUserName);

            if (optionalCurrentUser.isPresent()) { // Vérifiez si l'utilisateur a été trouvé
                Utilisateur currentUser = optionalCurrentUser.get();

                // Associer l'utilisateur expéditeur et destinataire
                message.setConversation(conversation);
                message.setFromUser(currentUser);
                message.setToUser(conversation.getAcheteur()); // Ou l'autre utilisateur selon le contexte

                return messageRepository.save(message);
            } else {
                // Gestion de l'utilisateur non trouvé (ex. lancer une exception ou retourner une réponse appropriée)
                throw new IllegalArgumentException("Utilisateur non trouvé");
            }
        }
        return null; // Vous pourriez envisager de lancer une exception si la conversation est introuvable
    }

}
