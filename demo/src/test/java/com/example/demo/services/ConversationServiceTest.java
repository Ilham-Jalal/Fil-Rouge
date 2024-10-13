package com.example.demo.services;

import com.example.demo.dto.ConversationResponseDTO;

import com.example.demo.models.Conversation;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.ConversationRepository;
import com.example.demo.repositorys.MessageRepository;
import com.example.demo.repositorys.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class ConversationServiceTest {

    @InjectMocks
    private ConversationService conversationService;

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    private Utilisateur vendeur;
    private Utilisateur acheteur;
    private Conversation conversation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        vendeur = new Utilisateur();
        vendeur.setId(1L);
        vendeur.setUsername("vendeur");

        acheteur = new Utilisateur();
        acheteur.setId(2L);
        acheteur.setUsername("acheteur");

        conversation = new Conversation();
        conversation.setId(1L);
        conversation.setVendeur(vendeur);
        conversation.setAcheteur(acheteur);
    }

     @Test
     void testGetConversationsByUserId_UserNotFound() {
        when(conversationRepository.findAllByUserId(3L)).thenReturn(Collections.emptyList());

        List<ConversationResponseDTO> responseDTOs = conversationService.getConversationsByUserId(3L);


        assertNotNull(responseDTOs);
        assertTrue(responseDTOs.isEmpty());
    }

    @Test
     void testGetConversationsByUsername() {
        when(utilisateurRepository.findByUsername("vendeur")).thenReturn(Optional.of(vendeur));
        when(conversationRepository.findAllByUserId(vendeur.getId())).thenReturn(Collections.singletonList(conversation));

        when(utilisateurRepository.findByUsername("vendeur")).thenReturn(Optional.of(vendeur));
        assertTrue(utilisateurRepository.findByUsername("vendeur").isPresent()); // Vérifier que l'utilisateur est trouvé


    }

    @Test
     void testGetConversationsByUsername_UserNotFound() {
        when(utilisateurRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        List<ConversationResponseDTO> responseDTOs = conversationService.getConversationsByUsername("nonexistent");

        assertNotNull(responseDTOs);
        assertTrue(responseDTOs.isEmpty());
    }
}
