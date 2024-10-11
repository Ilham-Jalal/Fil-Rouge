import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AnnonceResponseDTO } from "../../core/dto/AnnonceResponseDTO";
import { AnnonceService } from "../../core/service/annonce.service";
import { CommentaireDto } from "../../core/dto/CommentaireDto";
import { CommentaireService } from "../../core/service/commentaire.service";
import { AuthService } from "../../core/service/auth-service.service";
import {ConversationService} from "../../core/service/conversation.service";
import {Conversation} from "../../core/model/Conversation";

@Component({
  selector: 'app-annonce-details',
  templateUrl: './annonce-details.component.html',
  styleUrls: ['./annonce-details.component.css']
})
export class AnnonceDetailsComponent implements OnInit {
  annonce: AnnonceResponseDTO | undefined;
  commentaires: CommentaireDto[] = [];
  newComment: string = '';
  currentUserId: string | null = null;
  commentsVisible: boolean = false;
  isDescriptionExpanded: boolean = false;
  messageFormVisible = false;

  constructor(
    private conversationService: ConversationService,
    private route: ActivatedRoute,
    private annonceService: AnnonceService,
    private commentaireService: CommentaireService,
    private authService: AuthService,
    private router: Router
  ) { }

  toggleMessageForm() {
    this.messageFormVisible = !this.messageFormVisible;
  }



  getCommentairesByAnnonce(annonceId: number): void {
    this.commentaireService.getCommentairesByAnnonce(annonceId).subscribe(
      (commentaires: CommentaireDto[]) => {
        this.commentaires = commentaires;
      }
    );
  }

  createCommentaire(annonceId: number | undefined): void {
    if (this.newComment.trim() === '') {
      alert('Comment cannot be empty');
      return;
    }

    const commentaireDto: {
      utilisateurName: string;
      dateCreation: string;
      utilisateurId: number;
      contenu: string;
      annonceId: number | undefined
    } = {
      utilisateurId: Number(this.currentUserId),
      contenu: this.newComment,
      annonceId: annonceId,
      utilisateurName: "User", // Replace with actual username
      dateCreation: new Date().toISOString(),
    };

    this.commentaireService.createCommentaire(commentaireDto, this.annonce?.id).subscribe(
      (newComment) => {
        this.commentaires.push(newComment);
        this.newComment = '';
      }
    );
  }

  updateCommentaire(commentaire: CommentaireDto): void {
    if (commentaire.id) {
      this.commentaireService.updateCommentaire(commentaire.id, commentaire).subscribe(
        (updatedComment) => {
          const index = this.commentaires.findIndex(c => c.id === updatedComment.id);
          if (index !== -1) {
            this.commentaires[index] = updatedComment;
          }
        },
        (error) => {
          console.error('Error updating comment', error);
        }
      );
    }
  }

  deleteCommentaire(commentaireId: number | undefined): void {
    if (commentaireId) {
      this.commentaireService.deleteCommentaire(commentaireId).subscribe(
        () => {
          this.commentaires = this.commentaires.filter(c => c.id !== commentaireId);
        },
        (error) => {
          console.error('Error deleting comment', error);
        }
      );
    }
  }

  toggleComments(): void {
    this.commentsVisible = !this.commentsVisible;
  }

  toggleDescription(): void {
    this.isDescriptionExpanded = !this.isDescriptionExpanded;
  }

  backToList(): void {
    this.router.navigate(['/annonce']);
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.annonceService.findById(id).subscribe(
        (data) => {
          this.annonce = data;
          this.getCommentairesByAnnonce(id);
        }
      );
    }

    this.authService.getCurrentUserId().subscribe(
      userId => {
        this.currentUserId = userId.toString();
      },
      error => {
        console.error('Error fetching current user ID:', error);
      }
    );
  }

  contacterVendeur() {
    if (!this.currentUserId || !this.annonce) {
      console.error('User ID or annonce details are missing');
      return;
    }

    this.conversationService.createOrGetConversation(this.annonce.vendeurId, Number(this.currentUserId))
      .subscribe(
        (conversation: Conversation) => {
          this.router.navigate(['/conversation', conversation.id]);
        },
        (error) => {
          console.error('Error creating conversation:', error);
        }
      );}
  protected readonly Number = Number;
}
