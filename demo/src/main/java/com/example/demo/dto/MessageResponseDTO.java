package com.example.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private Long fromUserId;
    private Long toUserId;
    private Long parentMessageId;
}