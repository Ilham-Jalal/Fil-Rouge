import { Component, OnInit } from '@angular/core';
import { ConversationResponseDTO } from "../../core/dto/ConversationResponseDTO";
import { ConversationService } from "../../core/service/conversation.service";
import { AuthService } from "../../core/service/auth-service.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-conversation-list',
  templateUrl: './conversation-list.component.html',
  styleUrls: ['./conversation-list.component.scss']
})
export class ConversationListComponent implements OnInit {
  conversations: ConversationResponseDTO[] = [];
  currentUserId: number | null = null;
  isLoading = true;

  constructor(
    private router: Router,
    private conversationService: ConversationService,
    private authService: AuthService // Service pour obtenir l'utilisateur courant
  ) {}

  ngOnInit(): void {
    this.authService.getCurrentUserId().subscribe(userId => {
      this.currentUserId = userId;
      if (this.currentUserId) {
        this.loadConversations(this.currentUserId);
      }
    });
  }

  afficherCoversation(conversation: ConversationResponseDTO): void {
    this.router.navigate(['/conversation', conversation.id]);
  }

  loadConversations(userId: number): void {
    this.conversationService.getConversationsForCurrentUser().subscribe( // Change this method if needed
      (conversations: ConversationResponseDTO[]) => {
        this.conversations = conversations;
        this.isLoading = false;
      },
      error => {
        console.error('Erreur lors du chargement des conversations:', error);
        this.isLoading = false;
      }
    );
  }
}
