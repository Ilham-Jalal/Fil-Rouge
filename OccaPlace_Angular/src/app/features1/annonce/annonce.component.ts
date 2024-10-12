import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import { AnnonceResponseDTO } from '../../core/dto/AnnonceResponseDTO';
import { Categorie } from '../../core/enum/Categorie';

import { AnnonceService } from '../../core/service/annonce.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-annonce',
  templateUrl: './annonce.component.html',
  styleUrls: ['./annonce.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AnnonceComponent implements OnInit {
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
  showSearchForm: boolean = false;
  categorieKeys = Object.keys(Categorie) as Array<keyof typeof Categorie>;

  constructor(
    private annonceService: AnnonceService,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.getAllAnnonces();
  }
  toggleSearchForm(): void {
    this.showSearchForm = !this.showSearchForm;
  }


  getAllAnnonces(): void {
    this.annonceService.getAllAnnonces().subscribe({
      next: (data) => this.annonces = data,
      error: (err) => console.error('Erreur lors de la récupération des annonces', err)
    });
  }

  viewDetails(annonceId: number): void {
    this.router.navigate(['/annonce-details', annonceId]);
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



  onAnnonceAdded(annonce: AnnonceResponseDTO): void {
    this.annonces.push(annonce);
  }


  searchAnnonces(): void {
    this.annonceService.searchAnnonces(this.searchQuery.titleOrDescription, this.searchQuery.category, this.searchQuery.priceMin, this.searchQuery.priceMax)
      .subscribe({
        next: (data) => this.annonces = data,
        error: (err) => console.error('Erreur lors de la recherche des annonces', err)
      });
  }



  protected readonly Categorie = Categorie;
}
