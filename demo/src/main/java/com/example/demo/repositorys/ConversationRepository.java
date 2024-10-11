package com.example.demo.repositorys;

import com.example.demo.models.Conversation;
import com.example.demo.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByVendeurAndAcheteur(Utilisateur vendeur, Utilisateur acheteur);
    Optional<Conversation> findByVendeurIdAndAcheteur_Id(Long vendeurId, Long acheteurId);
    @Query("SELECT c FROM Conversation c WHERE c.acheteur.id = :userId OR c.vendeur.id = :userId")
    List<Conversation> findAllByUserId(Long userId);
}
