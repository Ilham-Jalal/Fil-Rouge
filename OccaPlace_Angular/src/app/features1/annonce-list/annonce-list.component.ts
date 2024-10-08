import { Component, OnInit } from '@angular/core';
import {AnnonceResponseDTO} from "../../core/dto/AnnonceResponseDTO";
import {Categorie} from "../../core/enum/Categorie";
import {AnnonceService} from "../../core/service/annonce.service";


@Component({
  selector: 'app-annonce-list',
  templateUrl: './annonce-list.component.html',
  styleUrls: ['./annonce-list.component.css']
})
export class AnnonceListComponent implements OnInit {
  annonces: AnnonceResponseDTO[] = [];
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

  constructor(private annonceService: AnnonceService) {}

  ngOnInit(): void {
    this.getAllAnnonces();
  }

  getAllAnnonces(): void {
    this.annonceService.getAllAnnonces().subscribe(
      (data) => {
        this.annonces = data;
      },
      (error) => {
        console.error('Error fetching annonces:', error);
      }
    );
  }

  resetSearch(): void {
    this.searchQuery = {
      titleOrDescription: '',
      category: Categorie.AUTRE,
      priceMin: 0,
      priceMax: 10000
    };
    this.getAllAnnonces();
  }

  searchAnnonces(): void {
    console.log('Searching with:', this.searchQuery);
    this.annonceService.searchAnnonces(this.searchQuery.titleOrDescription, this.searchQuery.category, this.searchQuery.priceMin, this.searchQuery.priceMax)
      .subscribe({
        next: (data) => this.annonces = data,
        error: (err) => console.error('Erreur lors de la recherche des annonces', err)
      });
  }


  protected readonly Categorie = Categorie;
}
