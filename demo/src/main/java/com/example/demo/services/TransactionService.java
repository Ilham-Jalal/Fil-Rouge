package com.example.demo.services;

import com.example.demo.models.Transaction;
import com.example.demo.models.Utilisateur;
import com.example.demo.repositorys.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByUtilisateur(Utilisateur utilisateur) {
        return transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getVendeur().getId().equals(utilisateur.getId()) ||
                        transaction.getAcheteur().getId().equals(utilisateur.getId()))
                .toList();
    }
}
