package com.example.demo.repositorys;

import com.example.demo.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByAcheteur_Id(Long userId);
}
