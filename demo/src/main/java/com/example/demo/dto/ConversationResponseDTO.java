package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponseDTO {
    private Long id;
    private String acheteurUsername;
    private String vendeurUsername;
    private String lastMessage;
    private LocalDateTime lastMessageDate;
}
