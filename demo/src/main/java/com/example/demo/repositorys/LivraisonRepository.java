package com.example.demo.repositorys;

import com.example.demo.models.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
}
