package com.example.demo.repositorys;

import com.example.demo.models.Conversation;
import com.example.demo.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByVendeurAndAcheteur(Utilisateur vendeur, Utilisateur acheteur);
}
