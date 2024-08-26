package com.example.demo.repositorys;

import com.example.demo.models.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    Livreur findByUsername(String username);
}
