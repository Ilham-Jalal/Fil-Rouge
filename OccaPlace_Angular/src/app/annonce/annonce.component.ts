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

  selectAnnonce(annonce: AnnonceResponseDTO): void {
    this.selectedAnnonce = annonce;
    this.updateAnnonceData = {
      title: annonce.title,
      description: annonce.description,
      price: annonce.price,
      category: annonce.category,
      disponibilite: annonce.disponibilite
    };
    this.getCommentairesForAnnonce(annonce.id);
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

  updateAnnonce(): void {
    if (this.selectedAnnonce) {
      this.annonceService.updateAnnonce(this.selectedAnnonce.id, this.updateAnnonceData).subscribe({
        next: (updatedAnnonce) => {
          const index = this.annonces.findIndex(a => a.id === updatedAnnonce.id);
          if (index !== -1) {
            this.annonces[index] = updatedAnnonce;
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
        this.resetForm();
      },
      error: (err) => console.error('Erreur lors de la suppression de l\'annonce', err)
    });
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
  }
}
