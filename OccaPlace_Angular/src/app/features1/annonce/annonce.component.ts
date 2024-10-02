import { Component, OnInit } from '@angular/core';
import { AnnonceResponseDTO } from '../../core/dto/AnnonceResponseDTO';
import { Categorie } from '../../core/enum/Categorie';
import { Disponibilite } from '../../core/enum/Disponibilite';
import { AnnonceUpdateDTO } from '../../core/dto/AnnonceUpdateDTO';
import { AnnonceService } from '../../core/service/annonce.service';
import { CommentaireDto } from '../../core/dto/CommentaireDto';
import { CommentaireService } from '../../core/service/commentaire.service';

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
  updateAnnonceData: {
    vendeurName: string;
    vendeurEmail: string;
    livraisonId?: number;
    vendeurId: number;
    price: number;
    disponibilite: Disponibilite;
    description: string;
    acheteurId?: number;
    id: number;
    title: string;
    category: Categorie;
    creationDate: string
  } = {
    creationDate: "", id: 0, vendeurEmail: "", vendeurId: 0, vendeurName: "",
    title: '',
    description: '',
    price: 0,
    category: Categorie.AUTRE,
    disponibilite: Disponibilite.DISPONIBLE
  };
  editCommentaire?: CommentaireDto;
  selectedFiles: File[] = [];
  searchQuery: {
    titleOrDescription: string;
    category: Categorie;
    priceMin: number;
    priceMax: number;
  } = {
    titleOrDescription: '',
    category: Categorie.AUTRE,
    priceMin: 0,
    priceMax: 10000
  };

  categorieKeys = Object.keys(Categorie) as Array<keyof typeof Categorie>;
  disponibiliteKeys = Object.keys(Disponibilite);

  constructor(
    private annonceService: AnnonceService,
    private commentaireService: CommentaireService
  ) {}

  ngOnInit(): void {
    this.getAllAnnonces();
  }

  getAllAnnonces(): void {
    this.annonceService.getAllAnnonces().subscribe({
      next: (data) => this.annonces = data,
      error: (err) => console.error('Erreur lors de la récupération des annonces', err)
    });
  }



  updateAnnonce(): void {
    if (this.selectedAnnonce) {
      const updatedAnnonce: AnnonceUpdateDTO = {
        title: this.updateAnnonceData.title,
        description: this.updateAnnonceData.description,
        price: this.updateAnnonceData.price,
        category: this.updateAnnonceData.category,
        disponibilite: this.updateAnnonceData.disponibilite
      };

      this.annonceService.updateAnnonce(this.selectedAnnonce.id, updatedAnnonce).subscribe({
        next: (data) => {
          const index = this.annonces.findIndex(a => a.id === this.selectedAnnonce!.id);
          this.annonces[index] = data;
          this.resetForm();
        },
        error: (err) => console.error('Erreur lors de la mise à jour de l\'annonce', err)
      });
    }
  }
  resetSearch(): void {
    this.searchQuery = {
      titleOrDescription: '',
      category: Categorie.AUTRE,
      priceMin: 0,
      priceMax: 10000
    };
    this.getAllAnnonces(); // Optionally reload all annonces
  }

  deleteAnnonce(id: number): void {
    this.annonceService.deleteAnnonce(id).subscribe({
      next: () => {
        this.annonces = this.annonces.filter(annonce => annonce.id !== id);
      },
      error: (err) => console.error('Erreur lors de la suppression de l\'annonce', err)
    });
  }



  onFileChange(event: any): void {
    this.selectedFiles = Array.from(event.target.files);
  }

  resetForm(): void {
    this.updateAnnonceData = {
      creationDate: "", id: 0, vendeurEmail: "", vendeurId: 0, vendeurName: "",
      title: '',
      description: '',
      price: 0,
      category: Categorie.AUTRE,
      disponibilite: Disponibilite.DISPONIBLE
    };
    this.selectedFiles = [];
    this.selectedAnnonce = undefined;
  }

  selectAnnonce(annonce: AnnonceResponseDTO): void {
    this.selectedAnnonce = annonce;
    this.updateAnnonceData = { ...annonce };
  }

  createCommentaire(annonceId: number): void {
    if (this.newCommentaire.contenu.trim() === '') return;

    const commentaireDto1: CommentaireDto = {
      contenu: this.newCommentaire.contenu,
      annonceId: this.selectedAnnonce?.id,
      utilisateurName: 'User',
      dateCreation: new Date()
    };

    this.commentaireService.createCommentaire(commentaireDto1, annonceId).subscribe({
      next: (data) => {
        this.commentaires.push(data);
        this.newCommentaire.contenu = '';
      },
      error: (err) => console.error('Erreur lors de l\'ajout d\'un commentaire', err)
    });
  }

  deleteCommentaire(commentaireId: number | undefined): void {
    this.commentaireService.deleteCommentaire(commentaireId).subscribe({
      next: () => {
        this.commentaires = this.commentaires.filter(c => c.id !== commentaireId);
      },
      error: (err) => console.error('Erreur lors de la suppression du commentaire', err)
    });
  }

  updateCommentaire(commentaire: CommentaireDto): void {
    if (this.editCommentaire) {
      this.commentaireService.updateCommentaire(this.editCommentaire.id, this.editCommentaire).subscribe({
        next: () => {
          const index = this.commentaires.findIndex(c => c.id === this.editCommentaire!.id);
          this.commentaires[index] = this.editCommentaire!;
          this.editCommentaire = undefined;
        },
        error: (err) => console.error('Erreur lors de la mise à jour du commentaire', err)
      });
    } else {
      this.editCommentaire = commentaire;
    }
  }

  onAnnonceAdded(annonce: AnnonceResponseDTO): void {
    this.annonces.push(annonce);
  }

  onAnnonceSearched(annonces: AnnonceResponseDTO[]): void {
    this.annonces = annonces;
  }
// annonce.component.ts
  searchAnnonces(): void {
    this.annonceService.searchAnnonces(this.searchQuery.titleOrDescription, this.searchQuery.category, this.searchQuery.priceMin, this.searchQuery.priceMax)
      .subscribe({
        next: (data) => this.annonces = data,
        error: (err) => console.error('Erreur lors de la recherche des annonces', err)
      });
  }



  protected readonly Categorie = Categorie;
}
/////////////
