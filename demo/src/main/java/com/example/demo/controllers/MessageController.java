package com.example.demo.controllers;

import com.example.demo.dto.MessageCreateDTO;
import com.example.demo.dto.MessageResponseDTO;
import com.example.demo.models.Utilisateur;
import com.example.demo.services.MessageService;
import com.example.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/send/{toUserId}")
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @PathVariable Long toUserId,
            @RequestBody MessageCreateDTO messageCreateDTO) {

        // Récupération des informations d'authentification
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Vérification de l'utilisateur authentifié
        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Ajouter l'ID de l'utilisateur destinataire au DTO
        messageCreateDTO.setToUserId(toUserId);

        // Récupération de l'utilisateur authentifié
        Utilisateur fromUser = optionalUser.get();

        // Appel au service pour envoyer le message
        MessageResponseDTO messageResponse = messageService.sendMessage(messageCreateDTO, fromUser);

        // Retourne une réponse avec le message créé et un statut 201
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }


    @PostMapping("/reply/{parentMessageId}")
    public ResponseEntity<MessageResponseDTO> replyToMessage(
            @PathVariable Long parentMessageId,
            @RequestBody MessageCreateDTO messageCreateDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur fromUser = optionalUser.get();
        MessageResponseDTO replyMessage = messageService.replyToMessage(messageCreateDTO, parentMessageId, fromUser);
        return new ResponseEntity<>(replyMessage, HttpStatus.CREATED);
    }

    @GetMapping("/sent")
    public ResponseEntity<List<MessageResponseDTO>> getSentMessages() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur fromUser = optionalUser.get();
        List<MessageResponseDTO> messages = messageService.getSentMessages(fromUser.getId());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/received")
    public ResponseEntity<List<MessageResponseDTO>> getReceivedMessages() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur toUser = optionalUser.get();
        List<MessageResponseDTO> messages = messageService.getReceivedMessages(toUser.getId());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
