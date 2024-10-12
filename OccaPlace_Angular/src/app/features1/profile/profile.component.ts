import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from "../../core/service/auth-service.service";
import { AnnonceService } from "../../core/service/annonce.service";
import { LivraisonService } from "../../core/service/livraison.service";
import { MatDialog } from '@angular/material/dialog';
import { LivraisonDialogComponent } from "../livraison-dialog/livraison-dialog.component";
import {AnnonceResponseDTO} from "../../core/dto/AnnonceResponseDTO";
import {UpdateAnnonceDialogComponent} from "../update-annonce-dialog/update-annonce-dialog.component";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  utilisateur: any;
  annonces: AnnonceResponseDTO[] = [];
  loading = true;
  error = '';

  constructor(
    private authService: AuthService,
    private annonceService: AnnonceService,
    private livraisonService: LivraisonService,
    private router: Router,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.getCurrentUser();
  }


  getCurrentUser(): void {
    const username = this.authService.getCurrentUsername();
    if (username) {
      this.authService.findUtilisateurByUsername(username).subscribe(
        (user) => {
          console.log('Utilisateur récupéré:', user);
          this.utilisateur = user;
          this.getUserAnnonces();
        },
        (error) => {
          console.error('Erreur lors de la récupération de l\'utilisateur:', error);
          this.error = 'Erreur lors de la récupération de l\'utilisateur';
          this.loading = false;
        }
      );
    } else {
      console.error('Pas de nom d\'utilisateur trouvé');
      this.error = 'Pas de nom d\'utilisateur trouvé';
      this.loading = false;
    }
  }

  getUserAnnonces(): void {
    this.annonceService.getAnnoncesByUser().subscribe(
      (annonces) => {
        console.log('Annonces reçues:', annonces);
        this.annonces = annonces;
        this.loading = false;
      },
      (error) => {
        console.error('Erreur lors de la récupération des annonces:', error);
        this.error = 'Erreur lors de la récupération des annonces';
        this.loading = false;
      }
    );
  }



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
  openUpdateAnnonceDialog(annonce: AnnonceResponseDTO): void {
    const dialogRef = this.dialog.open(UpdateAnnonceDialogComponent, {
      width: '500px',
      data: annonce
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.annonceService.updateAnnonce(annonce.id, result).subscribe(
          () => {
            window.alert('Annonce mise à jour avec succès !');
            this.getUserAnnonces();
          },
          (error) => {
            window.alert('Erreur lors de la mise à jour de l\'annonce.');
            console.error('Erreur:', error);
          }
        );
      }
    });
  }


  deleteAnnonce(annonceId: number): void {
    this.annonceService.deleteAnnonce(annonceId).subscribe(
      () => {
        console.log('Annonce supprimée');
        this.getUserAnnonces();
      },
      (error) => {
        console.error('Erreur lors de la suppression de l\'annonce:', error);
      }
    );
  }
}
