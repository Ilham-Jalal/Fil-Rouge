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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class MessageServiceTest {

    private MessageService messageService;
    private MessageRepository messageRepository;
    private UtilisateurRepository utilisateurRepository;
    private ConversationRepository conversationRepository; // Added ConversationRepository

    @BeforeEach
    void setUp() {
        messageRepository = mock(MessageRepository.class);
        utilisateurRepository = mock(UtilisateurRepository.class);
        conversationRepository = mock(ConversationRepository.class); // Mock ConversationRepository
        messageService = new MessageService(messageRepository, utilisateurRepository, conversationRepository);
    }

    @Test
    void sendMessage_ShouldReturnMessageResponseDTO_WhenUserExists() {
        // Arrange
        MessageCreateDTO messageCreateDTO = new MessageCreateDTO();
        messageCreateDTO.setToUserId(1L);
        messageCreateDTO.setContent("Hello!");

        Utilisateur fromUser = new Utilisateur();
        fromUser.setId(2L);

        Utilisateur toUser = new Utilisateur();
        toUser.setId(1L);

        Conversation conversation = new Conversation();
        conversation.setId(1L);
        conversation.setAcheteur(fromUser);
        conversation.setVendeur(toUser);

        Message savedMessage = new Message();
        savedMessage.setId(3L);
        savedMessage.setContent("Hello!");
        savedMessage.setFromUser(fromUser);
        savedMessage.setToUser(toUser);
        savedMessage.setConversation(conversation);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(toUser));
        when(conversationRepository.findByVendeurAndAcheteur(fromUser, toUser)).thenReturn(Optional.of(conversation));
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        // Act
        MessageResponseDTO response = messageService.sendMessage(messageCreateDTO, fromUser);

        // Assert
        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals("Hello!", response.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void sendMessage_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        MessageCreateDTO messageCreateDTO = new MessageCreateDTO();
        messageCreateDTO.setToUserId(1L);
        messageCreateDTO.setContent("Hello!");

        Utilisateur fromUser = new Utilisateur();
        fromUser.setId(2L);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            messageService.sendMessage(messageCreateDTO, fromUser);
        });
    }

    @Test
    void replyToMessage_ShouldReturnMessageResponseDTO_WhenParentMessageExists() {
        // Arrange
        MessageCreateDTO messageCreateDTO = new MessageCreateDTO();
        messageCreateDTO.setContent("Reply!");

        Utilisateur fromUser = new Utilisateur();
        fromUser.setId(2L);

        Message parentMessage = new Message();
        parentMessage.setId(3L);
        parentMessage.setFromUser(new Utilisateur());
        parentMessage.getFromUser().setId(1L);

        Message savedReplyMessage = new Message();
        savedReplyMessage.setId(4L);
        savedReplyMessage.setContent("Reply!");
        savedReplyMessage.setFromUser(fromUser);
        savedReplyMessage.setToUser(parentMessage.getFromUser());
        savedReplyMessage.setParentMessage(parentMessage);

        when(messageRepository.findById(3L)).thenReturn(Optional.of(parentMessage));
        when(messageRepository.save(any(Message.class))).thenReturn(savedReplyMessage);

        // Act
        MessageResponseDTO response = messageService.replyToMessage(messageCreateDTO, 3L, fromUser);

        // Assert
        assertNotNull(response);
        assertEquals(4L, response.getId());
        assertEquals("Reply!", response.getContent());
    }

    @Test
    void replyToMessage_ShouldThrowMessageNotFoundException_WhenParentMessageDoesNotExist() {
        // Arrange
        MessageCreateDTO messageCreateDTO = new MessageCreateDTO();
        messageCreateDTO.setContent("Reply!");

        Utilisateur fromUser = new Utilisateur();
        fromUser.setId(2L);

        when(messageRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MessageNotFoundException.class, () -> {
            messageService.replyToMessage(messageCreateDTO, 3L, fromUser);
        });
    }

    @Test
    void getSentMessages_ShouldReturnListOfMessageResponseDTO() {
        // Arrange
        Utilisateur fromUser = new Utilisateur();
        fromUser.setId(2L);

        Message message1 = new Message();
        message1.setId(3L);
        message1.setContent("Hello!");
        message1.setFromUser(fromUser);
        message1.setToUser(new Utilisateur());

        Message message2 = new Message();
        message2.setId(4L);
        message2.setContent("Hi!");
        message2.setFromUser(fromUser);
        message2.setToUser(new Utilisateur());

        when(messageRepository.findByFromUserId(2L)).thenReturn(Arrays.asList(message1, message2));

        // Act
        List<MessageResponseDTO> responses = messageService.getSentMessages(2L);

        // Assert
        assertEquals(2, responses.size());
        assertEquals(3L, responses.get(0).getId());
        assertEquals(4L, responses.get(1).getId());
    }

    @Test
    void getReceivedMessages_ShouldReturnListOfMessageResponseDTO() {
        // Arrange
        Utilisateur toUser = new Utilisateur();
        toUser.setId(2L);

        Message message1 = new Message();
        message1.setId(3L);
        message1.setContent("Hello!");
        message1.setFromUser(new Utilisateur());
        message1.setToUser(toUser);

        Message message2 = new Message();
        message2.setId(4L);
        message2.setContent("Hi!");
        message2.setFromUser(new Utilisateur());
        message2.setToUser(toUser);

        when(messageRepository.findByToUserId(2L)).thenReturn(Arrays.asList(message1, message2));

        // Act
        List<MessageResponseDTO> responses = messageService.getReceivedMessages(2L);

        // Assert
        assertEquals(2, responses.size());
        assertEquals(3L, responses.get(0).getId());
        assertEquals(4L, responses.get(1).getId());
    }
}
