import { Component, OnInit } from '@angular/core';
import {AnnonceResponseDTO} from "../dto/AnnonceResponseDTO";
import {AnnonceService} from "../service/annonce.service";


@Component({
  selector: 'app-annonce-list',
  templateUrl: './annonce-list.component.html',
  styleUrls: ['./annonce-list.component.scss']
})
export class AnnonceListComponent implements OnInit {
  annonces: AnnonceResponseDTO[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private annonceService: AnnonceService) {}

  ngOnInit(): void {
    this.fetchAnnonces();
  }

  fetchAnnonces(): void {
    this.annonceService.getAnnoncesByUser().subscribe({
      next: (data) => {
        this.annonces = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Une erreur est survenue lors de la récupération des annonces.';
        this.isLoading = false;
        console.error(error);
      }
    });
  }
}
