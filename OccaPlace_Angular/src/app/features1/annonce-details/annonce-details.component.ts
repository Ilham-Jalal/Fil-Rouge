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
  currentUserId: string | null = null;
  commentsVisible: boolean = false;
  isDescriptionExpanded: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private annonceService: AnnonceService,
    private commentaireService: CommentaireService,
    private authService: AuthService,
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

  createNewComment(): void {
    if (this.newComment.trim() && this.annonce?.id && this.currentUserId) {
      const commentaireDto: CommentaireDto = {
        utilisateurName: "",
        contenu: this.newComment,
        utilisateurId: Number(this.currentUserId),
        annonceId: this.annonce.id,
        dateCreation: new Date().toISOString() // assurez-vous que le format est compatible avec votre backend
      };

      this.commentaireService.createCommentaire(commentaireDto, this.annonce.id).subscribe(
        (newComment) => {
          this.commentaires.push(newComment);
          this.newComment = '';
        }
      );
    }
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
}
