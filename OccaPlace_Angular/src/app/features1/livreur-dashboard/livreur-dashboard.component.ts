import { Component, OnInit } from '@angular/core';
import {Livraison} from "../../core/model/Livraison";
import {LivraisonService} from "../../core/service/livraison.service";


@Component({
  selector: 'app-livreur-dashboard',
  templateUrl: './livreur-dashboard.component.html',
  styleUrls: ['./livreur-dashboard.component.scss']
})
export class LivreurDashboardComponent implements OnInit {
  livraisons: Livraison[] = [];

  constructor(private livraisonService: LivraisonService) { }

  ngOnInit(): void {
    this.getUserLivraisons();
  }

  getUserLivraisons(): void {
    this.livraisonService.getLivreurLivraisons().subscribe(
      (data: Livraison[]) => {
        this.livraisons = data;
      },
      (error) => {
        console.error('Erreur lors du chargement des livraisons', error);
      }
    );
  }

  confirmerLivraison(livraisonId: number): void {
    const montant = 100; // Exemple de montant
    this.livraisonService.confirmerLivraison(livraisonId, montant).subscribe(
      (response) => {
        console.log('Livraison confirmée avec succès', response);
        this.getUserLivraisons();
      },
      (error) => {
        console.error('Erreur lors de la confirmation de la livraison', error);
      }
    );
  }
}
