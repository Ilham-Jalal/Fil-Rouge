package com.example.demo.repositorys;

import com.example.demo.models.Livreur;
import com.example.demo.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    Optional<Livreur> findByUsername(String username);

}
