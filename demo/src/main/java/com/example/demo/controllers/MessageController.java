package com.example.demo.controllers;

import com.example.demo.models.Message;
import com.example.demo.models.Utilisateur;
import com.example.demo.services.MessageService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/send/{toUserId}")
    public ResponseEntity<Message> sendMessage(@PathVariable Long toUserId, @RequestBody String content) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur fromUser = optionalUser.get();
        Message message = messageService.sendMessage(fromUser, toUserId, content);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    @PostMapping("/reply/{parentMessageId}")
    public ResponseEntity<Message> replyToMessage(@PathVariable Long parentMessageId, @RequestBody String content) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur fromUser = optionalUser.get();
        Message replyMessage = messageService.replyToMessage(fromUser, parentMessageId, content);
        return new ResponseEntity<>(replyMessage, HttpStatus.CREATED);
    }

    @GetMapping("/sent")
    public ResponseEntity<List<Message>> getSentMessages() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur fromUser = optionalUser.get();
        List<Message> messages = messageService.getSentMessages(fromUser.getId());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/received")
    public ResponseEntity<List<Message>> getReceivedMessages() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Utilisateur> optionalUser = userService.findUtilisateurByUsername(username);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utilisateur toUser = optionalUser.get();
        List<Message> messages = messageService.getReceivedMessages(toUser.getId());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
