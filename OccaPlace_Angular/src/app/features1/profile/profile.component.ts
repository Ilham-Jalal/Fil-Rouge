import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from "../../core/service/auth-service.service";
import { AnnonceService } from "../../core/service/annonce.service";
import { LivraisonService } from "../../core/service/livraison.service";
import { MatDialog } from '@angular/material/dialog';
import { LivraisonDialogComponent } from "../livraison-dialog/livraison-dialog.component";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  utilisateur: any;
  annonces: any[] = [];

  constructor(
    private authService: AuthService,
    private annonceService: AnnonceService,
    private livraisonService: LivraisonService,
    private router: Router,
    public dialog: MatDialog  // Inject MatDialog
  ) {}

  ngOnInit(): void {
    this.getCurrentUser();
  }

  // Fetch the current user's details
  getCurrentUser(): void {
    const username = this.authService.getCurrentUsername();
    if (username) {
      this.authService.findUtilisateurByUsername(username).subscribe(
        (user) => {
          this.utilisateur = user;
          this.getUserAnnonces();
        },
        (error) => {
          console.error('Erreur lors de la récupération de l\'utilisateur:', error);
        }
      );
    }
  }

  // Fetch the user's annonces (ads)
  getUserAnnonces(): void {
    this.annonceService.getAnnoncesByUser().subscribe(
      (annonces) => {
        this.annonces = annonces;
      },
      (error) => {
        console.error('Erreur lors de la récupération des annonces:', error);
      }
    );
  }

  // Open the dialog for creating a new delivery
  openLivraisonDialog(annonceId: number): void {
    const dialogRef = this.dialog.open(LivraisonDialogComponent, {
      width: '400px',
      data: null

    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.creerLivraison(result, annonceId);
      }
    });
  }

  creerLivraison(livraisonData: any, annonceId: number): void {
    const livraison = {
      ...livraisonData,
      annonceId
    };

    this.livraisonService.createLivraison(livraison).subscribe(
      (response) => {
        window.alert('Livraison créée avec succès !');

        this.livraisonService.associerLivraison(annonceId, response.id).subscribe(
          (updatedAnnonce) => {
            this.getUserAnnonces();
          },
          (error) => {
            window.alert('Erreur lors de l\'association de la livraison à l\'annonce.');
            console.error('Erreur lors de l\'association de la livraison à l\'annonce:', error);
          }
        );
      },
      (error) => {
        window.alert('Erreur lors de la création de la livraison.');
        console.error('Erreur lors de la création de la livraison:', error);
      }
    );
  }



  // Optional: Methods for updating and deleting an announcement
  updateAnnonce(annonceId: number): void {
    this.router.navigate([`/annonce/update`, annonceId]);
  }

  deleteAnnonce(annonceId: number): void {
    this.annonceService.deleteAnnonce(annonceId).subscribe(
      () => {
        console.log('Annonce supprimée');
        this.getUserAnnonces(); // Refresh the list after deletion
      },
      (error) => {
        console.error('Erreur lors de la suppression de l\'annonce:', error);
      }
    );
  }
}
