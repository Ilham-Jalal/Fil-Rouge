package com.example.demo.controllers;

import com.example.demo.models.Transaction;
import com.example.demo.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactionHistory() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long utilisateurId = Long.valueOf(auth.getName());

        List<Transaction> transactions = transactionService.getTransactionsByUtilisateur(utilisateurId);
        return ResponseEntity.ok(transactions);
    }
}
