import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Conversation } from "../../core/model/Conversation";
import { ConversationService } from "../../core/service/conversation.service";
import { MessageResponseDTO } from "../../core/dto/MessageResponseDTO";
import { AnnonceResponseDTO } from "../../core/dto/AnnonceResponseDTO";
import { AnnonceService } from "../../core/service/annonce.service";
import { AuthService } from "../../core/service/auth-service.service";

@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.css']
})
export class ConversationComponent implements OnInit {
  conversation: Conversation | null = null;
  messages: MessageResponseDTO[] = [];
  newMessage: string = '';
  editingMessage: MessageResponseDTO | null = null;
  currentUser: string | null = null;
  currentUserId: string | null = null;
  isLoading: boolean = false;
  toUserId: number = 0;
  charCount: number = 0;
  editCharCount: number = 0;
  annonceDetails: AnnonceResponseDTO | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private conversationService: ConversationService,
    private annonceService: AnnonceService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const conversationId = this.route.snapshot.paramMap.get('id');
    const annonceId = this.route.snapshot.paramMap.get('annonceId');

    if (conversationId) {
      this.loadConversation(+conversationId);
      this.loadMessages(+conversationId);
    }

    if (annonceId) {
      this.loadAnnonceDetails(+annonceId);
    }

    this.currentUserId = this.authService.getCurrentUserId();
    if (this.currentUserId) {
      this.authService.findUtilisateurByUsername(this.currentUserId).subscribe(
        user => {
          this.currentUser = user.username || 'Utilisateur inconnu';
        },
        error => {
          console.error('Erreur lors de la récupération du nom d\'utilisateur:', error);
          this.currentUser = 'Utilisateur inconnu';
        }
      );
    } else {
      this.currentUser = 'Utilisateur inconnu';
    }
  }

  loadConversation(id: number): void {
    this.conversationService.getConversation(id).subscribe(
      (conversation: Conversation) => {
        this.conversation = conversation;
        this.toUserId = conversation.vendeur.id === Number(this.currentUserId) ? conversation.acheteur.id : conversation.vendeur.id;
      },
      error => console.error('Erreur lors du chargement de la conversation:', error)
    );
  }

  loadMessages(conversationId: number): void {
    this.conversationService.getMessages(conversationId).subscribe(
      messages => {
        this.messages = messages;
      },
      error => console.error('Erreur lors du chargement des messages:', error)
    );
  }

  loadAnnonceDetails(annonceId: number): void {
    this.annonceService.findById(annonceId).subscribe(
      (annonce: AnnonceResponseDTO) => {
        this.annonceDetails = annonce;
      },
      error => console.error('Erreur lors du chargement des détails de l\'annonce:', error)
    );
  }

  sendMessage(): void {
    if (!this.newMessage.trim() || !this.toUserId) {
      return;
    }

    this.isLoading = true;
    const message: { toUserId: number; content: string } = { toUserId: this.toUserId, content: this.newMessage };

    this.conversationService.sendMessage(this.conversation!.id, message).subscribe(
      response => {
        this.messages.push(response);
        this.newMessage = '';
        this.charCount = 0;
        this.isLoading = false;
      },
      error => {
        console.error('Erreur lors de l\'envoi du message:', error);
        this.isLoading = false;
      }
    );
  }

  canEditOrDeleteMessage(message: MessageResponseDTO): boolean {
    return message.fromUserName === this.currentUser;
  }

  deleteMessage(message: MessageResponseDTO): void {
    if (!this.canEditOrDeleteMessage(message)) {
      console.error('Vous n\'avez pas le droit de supprimer ce message.');
      return;
    }

    if (confirm('Êtes-vous sûr de vouloir supprimer ce message ?')) {
      this.conversationService.deleteMessage(message.id).subscribe(
        () => {
          this.messages = this.messages.filter(m => m.id !== message.id);
        },
        error => console.error('Erreur lors de la suppression du message:', error)
      );
    }
  }

  startEditing(message: MessageResponseDTO): void {
    if (!this.canEditOrDeleteMessage(message)) {
      console.error('Vous n\'avez pas le droit de modifier ce message.');
      return;
    }

    this.editingMessage = { ...message };
    this.editCharCount = this.editingMessage.content.length;
  }

  updateMessage(): void {
    if (this.editingMessage && this.editingMessage.content.trim()) {
      this.conversationService.updateMessage(this.editingMessage.id, this.editingMessage.content).subscribe(
        updatedMessage => {
          const index = this.messages.findIndex(m => m.id === updatedMessage.id);
          if (index > -1) {
            this.messages[index] = updatedMessage;
          }
          this.editingMessage = null;
          this.editCharCount = 0;
        },
        error => console.error('Erreur lors de la mise à jour du message:', error)
      );
    }
  }

  cancelEditing(): void {
    this.editingMessage = null;
    this.editCharCount = 0;
  }

  updateCharCount(): void {
    this.charCount = this.newMessage.length;
  }

  updateEditCharCount(): void {
    if (this.editingMessage) {
      this.editCharCount = this.editingMessage.content.length;
    }
  }

  backToAnnonce(): void {
    if (this.annonceDetails) {
      this.router.navigate(['/annonce', this.annonceDetails.id]);
    } else {
      this.router.navigate(['/annonces']);
    }
  }
}
