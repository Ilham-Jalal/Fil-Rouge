import { Component, OnInit } from '@angular/core';
import { AnnonceResponseDTO } from '../dto/AnnonceResponseDTO';
import { AnnonceCreateDTO } from '../dto/AnnonceCreateDTO';
import { Categorie } from '../enum/Categorie';
import { Disponibilite } from '../enum/Disponibilite';
import { AnnonceUpdateDTO } from '../dto/AnnonceUpdateDTO';
import { AnnonceService } from '../service/annonce.service';
import { CommentaireDto } from '../dto/CommentaireDto';
import { CommentaireService } from '../service/commentaire.service';

@Component({
  selector: 'app-annonce',
  templateUrl: './annonce.component.html',
  styleUrls: ['./annonce.component.css']
})
export class AnnonceComponent implements OnInit {
  annonces: AnnonceResponseDTO[] = [];
  commentaires: CommentaireDto[] = [];
  newCommentaire: { contenu: string } = { contenu: '' };
  selectedAnnonce?: AnnonceResponseDTO;
  updateAnnonceData: AnnonceUpdateDTO = {};
  editCommentaire?: CommentaireDto;

  categorieKeys = Object.keys(Categorie);
  disponibiliteKeys = Object.keys(Disponibilite);

  constructor(
    private annonceService: AnnonceService,
    private commentaireService: CommentaireService
  ) { }

  ngOnInit(): void {
    this.getAllAnnonces();
  }

  getAllAnnonces(): void {
    this.annonceService.getAllAnnonces().subscribe({
      next: (data) => this.annonces = data,
      error: (err) => console.error('Erreur lors de la récupération des annonces', err)
    });
  }

  createAnnonce(): void {
    const annonceToCreate: AnnonceCreateDTO = {
      title: this.updateAnnonceData.title || '',
      description: this.updateAnnonceData.description || '',
      price: this.updateAnnonceData.price || 0,
      category: this.updateAnnonceData.category || Categorie.AUTRE,
      disponibilite: this.updateAnnonceData.disponibilite || Disponibilite.DISPONIBLE
    };

    this.annonceService.createAnnonce(annonceToCreate).subscribe({
      next: (data) => {
        this.annonces.push(data);
        this.resetForm();
      },
      error: (err) => console.error('Erreur lors de la création de l\'annonce', err)
    });
  }

  updateAnnonce(): void {
    if (this.selectedAnnonce) {
      const updatedAnnonce: AnnonceUpdateDTO = {
        title: this.updateAnnonceData.title || '',
        description: this.updateAnnonceData.description || '',
        price: this.updateAnnonceData.price || 0,
        category: this.updateAnnonceData.category || Categorie.AUTRE,
        disponibilite: this.updateAnnonceData.disponibilite || Disponibilite.DISPONIBLE
      };

      this.annonceService.updateAnnonce(this.selectedAnnonce.id, updatedAnnonce).subscribe({
        next: () => {
          const index = this.annonces.findIndex(a => a.id === this.selectedAnnonce?.id);
          if (index !== -1) {
            this.annonces[index] = <AnnonceResponseDTO>{
              creationDate: "",
              id: 0,
              vendeurId: 0,
              vendeurName: "", ...this.selectedAnnonce, ...updatedAnnonce
            };
          }
          this.resetForm();
        },
        error: (err) => console.error('Erreur lors de la mise à jour de l\'annonce', err)
      });
    }
  }

  deleteAnnonce(id: number): void {
    this.annonceService.deleteAnnonce(id).subscribe({
      next: () => {
        this.annonces = this.annonces.filter(a => a.id !== id);
      },
      error: (err) => console.error('Erreur lors de la suppression de l\'annonce', err)
    });
  }

  selectAnnonce(annonce: AnnonceResponseDTO): void {
    this.selectedAnnonce = { ...annonce };
    this.updateAnnonceData = {
      title: this.selectedAnnonce.title,
      description: this.selectedAnnonce.description,
      price: this.selectedAnnonce.price,
      category: this.selectedAnnonce.category,
      disponibilite: this.selectedAnnonce.disponibilite
    };
    this.getCommentairesForAnnonce(this.selectedAnnonce.id);
  }

  getCommentairesForAnnonce(annonceId: number): void {
    this.commentaireService.getCommentairesByAnnonce(annonceId).subscribe({
      next: (data) => this.commentaires = data,
      error: (err) => console.error('Erreur lors de la récupération des commentaires', err)
    });
  }

  createCommentaire(): void {
    if (this.selectedAnnonce && this.newCommentaire.contenu) {
      const commentaireToAdd: CommentaireDto = {
        contenu: this.newCommentaire.contenu,
        dateCreation: new Date(),
        id: 0,
        annonceId: this.selectedAnnonce.id,
        utilisateurName: ''
      };

      this.commentaireService.createCommentaire(commentaireToAdd, this.selectedAnnonce.id).subscribe({
        next: (commentaire) => {
          this.commentaires.push(commentaire);
          this.newCommentaire.contenu = '';
        },
        error: (err) => console.error('Erreur lors de l\'ajout du commentaire', err)
      });
    }
  }

  updateCommentaire(commentaire: CommentaireDto): void {
    if (this.editCommentaire && this.selectedAnnonce) {
      this.commentaireService.updateCommentaire(this.editCommentaire.id, this.editCommentaire).subscribe({
        next: (updatedCommentaire) => {
          const index = this.commentaires.findIndex(c => c.id === updatedCommentaire.id);
          if (index !== -1) {
            this.commentaires[index] = updatedCommentaire;
          }
          this.editCommentaire = undefined; // Réinitialiser après la mise à jour
        },
        error: (err) => console.error('Erreur lors de la mise à jour du commentaire', err)
      });
    }
  }

  deleteCommentaire(id: number | undefined): void {
    if (id !== undefined) {
      this.commentaireService.deleteCommentaire(id).subscribe({
        next: () => {
          this.commentaires = this.commentaires.filter(c => c.id !== id);
        },
        error: (err) => console.error('Erreur lors de la suppression du commentaire', err)
      });
    }
  }

  editComment(commentaire: CommentaireDto): void {
    this.editCommentaire = { ...commentaire };
  }

  cancelEdit(): void {
    this.editCommentaire = undefined;
  }

  resetForm(): void {
    this.updateAnnonceData = {
      title: '',
      description: '',
      price: 0,
      category: Categorie.AUTRE,
      disponibilite: Disponibilite.DISPONIBLE
    };
    this.selectedAnnonce = undefined;
    this.commentaires = [];
    this.editCommentaire = undefined;
  }
}
