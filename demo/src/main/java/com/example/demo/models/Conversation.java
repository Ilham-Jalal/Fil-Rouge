package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendeur_id", referencedColumnName = "id")
    private Utilisateur vendeur;

    @ManyToOne
    @JoinColumn(name = "acheteur_id", referencedColumnName = "id")
    private Utilisateur acheteur;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messages;


    public void addMessage(Message message) {
        messages.add(message);
        message.setConversation(this);
    }

    public Message getLastMessage() {
        return this.messages.stream()
                .max(Comparator.comparing(Message::getTimestamp))
                .orElse(null);  // Si aucun message, retourne null
    }

    // Retourne la date du dernier message
    public LocalDateTime getLastMessageDate() {
        Message lastMessage = getLastMessage();
        return lastMessage != null ? lastMessage.getTimestamp() : null;
    }
}
