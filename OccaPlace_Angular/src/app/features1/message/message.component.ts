import { Component } from '@angular/core';
import {MessageCreateDTO} from "../../core/dto/MessageCreateDTO";
import {MessageResponseDTO} from "../../core/dto/MessageResponseDTO";
import {MessageService} from "../../core/service/message.service";

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css'],
})
export class MessageComponent {
  message: MessageCreateDTO = { toUserId: 0, content: '' }; // Initialisation du message
  sentMessages: MessageResponseDTO[] = [];
  receivedMessages: MessageResponseDTO[] = [];

  constructor(private messageService: MessageService) {}

  sendMessage() {
    this.messageService.sendMessage(this.message.toUserId, this.message).subscribe({
      next: (response: MessageResponseDTO) => {
        console.log('Message sent:', response);
        this.message.content = ''; // Réinitialiser le contenu après l'envoi
      },
      error: (error) => {
        console.error('Error sending message:', error);
      },
    });
  }

  getSentMessages() {
    this.messageService.getSentMessages().subscribe({
      next: (messages) => {
        this.sentMessages = messages; // Stocker les messages envoyés
        console.log('Sent Messages:', messages);
      },
      error: (error) => {
        console.error('Error fetching sent messages:', error);
      },
    });
  }

  getReceivedMessages() {
    this.messageService.getReceivedMessages().subscribe({
      next: (messages) => {
        this.receivedMessages = messages; // Stocker les messages reçus
        console.log('Received Messages:', messages);
      },
      error: (error) => {
        console.error('Error fetching received messages:', error);
      },
    });
  }
}
