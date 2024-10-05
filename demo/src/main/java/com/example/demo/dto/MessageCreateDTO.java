package com.example.demo.dto;

import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageCreateDTO {
    private Long toUserId;
    private String content;

}
