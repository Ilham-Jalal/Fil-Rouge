package com.example.demo.services;

import com.example.demo.models.Message;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.MessageRepository;
import com.example.demo.repositorys.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMessage() {
        // Given
        Utilisateur fromUser = new Utilisateur();
        fromUser.setId(1L);
        Utilisateur toUser = new Utilisateur();
        toUser.setId(2L);

        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(toUser));

        Message message = new Message();
        message.setFromUser(fromUser);
        message.setToUser(toUser);
        message.setContent("Hello!");

        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // When
        Message result = messageService.sendMessage(fromUser, 2L, "Hello!");

        // Then
        assertNotNull(result);
        assertEquals(fromUser, result.getFromUser());
        assertEquals(toUser, result.getToUser());
        assertEquals("Hello!", result.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testSendMessageUserNotFound() {
        // Given
        Utilisateur fromUser = new Utilisateur();
        fromUser.setId(1L);

        when(utilisateurRepository.findById(2L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> {
            messageService.sendMessage(fromUser, 2L, "Hello!");
        });
    }

    @Test
    void testReplyToMessage() {
        // Given
        Utilisateur fromUser = new Utilisateur();
        fromUser.setId(1L);
        Message parentMessage = new Message();
        parentMessage.setId(1L);
        parentMessage.setFromUser(new Utilisateur());
        parentMessage.getFromUser().setId(2L);

        when(messageRepository.findById(1L)).thenReturn(Optional.of(parentMessage));

        Message replyMessage = new Message();
        replyMessage.setFromUser(fromUser);
        replyMessage.setToUser(parentMessage.getFromUser());
        replyMessage.setContent("Reply to your message");
        replyMessage.setParentMessage(parentMessage);

        when(messageRepository.save(any(Message.class))).thenReturn(replyMessage);

        // When
        Message result = messageService.replyToMessage(fromUser, 1L, "Reply to your message");

        // Then
        assertNotNull(result);
        assertEquals(fromUser, result.getFromUser());
        assertEquals(parentMessage.getFromUser(), result.getToUser());
        assertEquals("Reply to your message", result.getContent());
        assertEquals(parentMessage, result.getParentMessage());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testReplyToMessageNotFound() {
        // Given
        Utilisateur fromUser = new Utilisateur();
        fromUser.setId(1L);

        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> {
            messageService.replyToMessage(fromUser, 1L, "Reply");
        });
    }

    @Test
    void testGetSentMessages() {
        // Given
        Utilisateur fromUser = new Utilisateur();
        fromUser.setId(1L);
        List<Message> messages = List.of(new Message(), new Message());

        when(messageRepository.findByFromUserId(1L)).thenReturn(messages);

        // When
        List<Message> result = messageService.getSentMessages(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(messageRepository, times(1)).findByFromUserId(1L);
    }

    @Test
    void testGetReceivedMessages() {
        // Given
        Utilisateur toUser = new Utilisateur();
        toUser.setId(1L);
        List<Message> messages = List.of(new Message(), new Message());

        when(messageRepository.findByToUserId(1L)).thenReturn(messages);

        // When
        List<Message> result = messageService.getReceivedMessages(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(messageRepository, times(1)).findByToUserId(1L);
    }
}
