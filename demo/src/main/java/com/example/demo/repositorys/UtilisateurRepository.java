package com.example.demo.repositorys;

import com.example.demo.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByUsername(String username);
    @Query("SELECT u.id FROM Utilisateur u WHERE u.username = :username")
    Integer findIdByUsername(@Param("username") String username);
}
