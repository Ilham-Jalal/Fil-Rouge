package com.example.demo.controllers;

import com.example.demo.dto.ConversationResponseDTO;
import com.example.demo.dto.MessageResponseDTO;
import com.example.demo.models.Conversation;
import com.example.demo.models.Message;
import com.example.demo.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    @Autowired
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/")
    public ResponseEntity<Conversation> createConversation(@RequestBody Conversation conversation) {
        Conversation createdConversation = conversationService.createConversation(conversation);
        return new ResponseEntity<>(createdConversation, HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<Conversation> createOrGetConversation(@RequestParam Long vendeurId, @RequestParam Long acheteurId) {
        Conversation conversation = conversationService.createOrGetConversation(vendeurId, acheteurId);
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<List<ConversationResponseDTO>> getConversationsForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<ConversationResponseDTO> conversations = conversationService.getConversationsByUsername(username);

        if (conversations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(conversations);
    }
    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByConversationId(@PathVariable Long conversationId) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the conversation to check participants
        Conversation conversation = conversationService.getConversationById(conversationId);

        // Check if the authenticated user is part of the conversation
        if (conversation != null && (conversation.getVendeur().getUsername().equals(username) ||
                conversation.getAcheteur().getUsername().equals(username))) {
            List<MessageResponseDTO> messages = conversationService.getMessagesByConversationId(conversationId); // Utilisation du DTO
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Return forbidden if not authorized
        }
    }


    @GetMapping
    public ResponseEntity<List<Conversation>> getAllConversations() {
        List<Conversation> conversations = conversationService.getAllConversations();
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable Long id) {
        Conversation conversation = conversationService.getConversationById(id);
        return conversation != null ? new ResponseEntity<>(conversation, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id) {
        conversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<Message> addMessageToConversation(@PathVariable Long conversationId, @RequestBody Message message) {
        Message savedMessage = conversationService.addMessageToConversation(conversationId, message);
        return savedMessage != null ? new ResponseEntity<>(savedMessage, HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
