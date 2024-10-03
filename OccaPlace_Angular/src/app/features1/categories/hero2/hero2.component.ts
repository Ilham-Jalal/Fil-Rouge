import {Component, Output, EventEmitter} from '@angular/core';
import {AnnonceResponseDTO} from "../../../core/dto/AnnonceResponseDTO";
import {Categorie} from "../../../core/enum/Categorie";
import {Disponibilite} from "../../../core/enum/Disponibilite";
import {AnnonceService} from "../../../core/service/annonce.service";

@Component({
  selector: 'app-hero2',
  templateUrl: './hero2.component.html',
  styleUrls: ['./hero2.component.css']
})
export class Hero2Component {

  @Output() annonceAdded: EventEmitter<AnnonceResponseDTO> = new EventEmitter<AnnonceResponseDTO>();
  annonces: AnnonceResponseDTO[] = [];
  showAnnonceForm = false;
  updateAnnonceData = {
    title: '',
    description: '',
    price: 0,
    category: Categorie.AUTRE,
    disponibilite: Disponibilite.DISPONIBLE
  };
  selectedFiles: File[] = [];
  categorieKeys = Object.keys(Categorie) as Array<keyof typeof Categorie>;
  disponibiliteKeys = Object.keys(Disponibilite) as Array<keyof typeof Disponibilite>;

  constructor(private annonceService: AnnonceService) {}

  ngOnInit(): void {
    this.getAllAnnonces();
  }

  toggleAnnonceForm(): void {
    this.showAnnonceForm = !this.showAnnonceForm;
  }

  createAnnonceWithImages(): void {
    this.annonceService.createAnnonceWithImages(this.updateAnnonceData, this.selectedFiles).subscribe({
      next: (data: AnnonceResponseDTO) => {
        this.annonces.push(data);
        this.annonceAdded.emit(data);
        this.resetForm();
        this.showAnnonceForm = false;
      },
      error: (err) => {
        console.error('Erreur lors de la création de l\'annonce', err);
      }
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
    this.selectedFiles = [];
  }

  onFileChange(event: any): void {
    this.selectedFiles = Array.from(event.target.files);
  }

  getAllAnnonces(): void {
    this.annonceService.getAllAnnonces().subscribe({
      next: (data) => this.annonces = data,
      error: (err) => console.error('Erreur lors de la récupération des annonces', err)
    });
  }
}
