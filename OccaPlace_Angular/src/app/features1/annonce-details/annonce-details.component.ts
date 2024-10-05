import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AnnonceResponseDTO } from "../../core/dto/AnnonceResponseDTO";
import { AnnonceService } from "../../core/service/annonce.service";
import { CommentaireDto } from "../../core/dto/CommentaireDto";
import { CommentaireService } from "../../core/service/commentaire.service";
import {AuthService} from "../../core/service/auth-service.service";

@Component({
  selector: 'app-annonce-details',
  templateUrl: './annonce-details.component.html',
  styleUrls: ['./annonce-details.component.css']
})
export class AnnonceDetailsComponent implements OnInit {
  annonce: AnnonceResponseDTO | undefined;
  commentaires: CommentaireDto[] = [];
  newComment: string = '';
  currentUserId: string | null = null; // Get the current user's ID
  commentsVisible: boolean = false;
  isDescriptionExpanded: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private annonceService: AnnonceService,
    private commentaireService: CommentaireService,
    private authService: AuthService, // Inject AuthService to get user info
    private router: Router
  ) {}

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

    // Get the current user's ID
    this.currentUserId = this.authService.getCurrentUserId();
  }

  getCommentairesByAnnonce(annonceId: number): void {
    this.commentaireService.getCommentairesByAnnonce(annonceId).subscribe(
      (commentaires) => {
        this.commentaires = commentaires;
      },
      (error) => {
        console.error('Erreur lors de la récupération des commentaires', error);
      }
    );
  }

  createCommentaire(annonceId: number | undefined): void {
    if (this.newComment.trim() === '') return;

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
      utilisateurName: "User",
      dateCreation: new Date().toISOString(),
    };

    this.commentaireService.createCommentaire(commentaireDto, this.annonce?.id).subscribe(
      (newComment) => {
        this.commentaires.push(newComment);
        this.newComment = ''; // Réinitialiser le champ de texte
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
          console.error('Erreur lors de la mise à jour du commentaire', error);
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
          console.error('Erreur lors de la suppression du commentaire', error);
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

  protected readonly Number = Number;
}
