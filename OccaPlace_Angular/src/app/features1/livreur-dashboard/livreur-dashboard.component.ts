import { Component, OnInit } from '@angular/core';
import { Livraison } from "../../core/model/Livraison";
import { LivraisonService } from "../../core/service/livraison.service";
import { AuthService } from "../../core/service/auth-service.service";
import { Router } from "@angular/router";
import { UserDTO } from "../../core/dto/UserDTO";

@Component({
  selector: 'app-livreur-dashboard',
  templateUrl: './livreur-dashboard.component.html',
  styleUrls: ['./livreur-dashboard.component.scss']
})
export class LivreurDashboardComponent implements OnInit {
  livraisons: Livraison[] = [];
  menuOpen = false;
  livreur: UserDTO | null = null;

  constructor(
    private livraisonService: LivraisonService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getUserLivraisons();
    this.getCurrentLivreur();
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

  getCurrentLivreur(): void {
    const username = this.authService.getCurrentUsername();
    if (username) {
      this.authService.findUtilisateurByUsername(username).subscribe(
        (livreurData: UserDTO) => {  // Expecting a single UserDTO
          this.livreur = livreurData; // Set livreur to the single object
        },
        (error) => {
          console.error('Erreur lors de la récupération du livreur', error);
        }
      );
    }
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

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  onMenuClick(): void {
    this.menuOpen = !this.menuOpen;
  }

  closeMenu(): void {
    this.menuOpen = false;
  }
}
