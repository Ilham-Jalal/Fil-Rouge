import { Component, OnInit } from '@angular/core';
import { AnnonceResponseDTO } from '../dto/AnnonceResponseDTO';
import { Categorie } from '../enum/Categorie';
import { AnnonceService } from '../service/annonce.service';

@Component({
  selector: 'app-annonces-by-category',
  templateUrl: './annonces-by-category.component.html',
  styleUrls: ['./annonces-by-category.component.scss']
})
export class AnnoncesByCategoryComponent implements OnInit {

  annonces: AnnonceResponseDTO[] = [];
  selectedCategory: Categorie = Categorie.AUTRE;

  categories = Object.values(Categorie);

  constructor(private annonceService: AnnonceService) { }

  ngOnInit(): void {
    this.getAnnoncesByCategory(this.selectedCategory);
  }

  getAnnoncesByCategory(category: Categorie): void {
    this.annonceService.getAnnoncesByCategory(category)
      .subscribe({
        next: (data) => this.annonces = data,
        error: (err) => console.error('Erreur lors de la récupération des annonces :', err)
      });
  }

  onCategoryChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    const newCategory = target.value;
    console.log('Selected category:', newCategory); // Log la catégorie sélectionnée
    this.getAnnoncesByCategory(newCategory as Categorie); // Passez correctement la catégorie
  }

}
