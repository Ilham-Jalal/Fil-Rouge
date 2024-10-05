import { Component, Input } from '@angular/core';
import { AnnonceResponseDTO } from "../../core/dto/AnnonceResponseDTO";
import { MessageService } from "../../core/service/message.service";

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent {
  @Input() annonce: AnnonceResponseDTO | undefined;
  messageContent: string = '';

  constructor(private messageService: MessageService) {}

  sendMessage() {
    if (!this.messageContent.trim()) {
      alert('Message content cannot be empty');
      return;
    }

    if (this.annonce) {
      const vendeurId = this.annonce.vendeurId;
      this.messageService.sendMessage(vendeurId, this.messageContent)
        .subscribe({
          next: (response) => {
            console.log('Message sent', response);
            this.messageContent = '';
          },
          error: (error) => {
            console.error('Error sending message', error);
            alert('Failed to send message');
          }
        });
    }
  }
}
