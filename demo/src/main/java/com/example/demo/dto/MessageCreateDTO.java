package com.example.demo.dto;

import lombok.*;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageCreateDTO {
    private Long toUserId;
    private String content;

}
