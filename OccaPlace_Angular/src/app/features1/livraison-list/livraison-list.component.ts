import { Component, OnInit } from '@angular/core';
import { Livraison } from "../../core/model/Livraison";
import { Livreur } from "../../core/model/Livreur";
import { LivraisonService } from "../../core/service/livraison.service";
import { UserService } from "../../core/service/user-service.service";

@Component({
  selector: 'app-livraison-list',
  templateUrl: './livraison-list.component.html',
  styleUrls: ['./livraison-list.component.css']
})
export class LivraisonListComponent implements OnInit {
  livraisons: Livraison[] = [];
  livreurs: Livreur[] = [];
  livreursMap: Map<number, Livreur> = new Map();

  displayedColumns: string[] = ['id', 'adresseVendeur', 'telephoneVendeur','adresseAcheteur','telephoneAcheteur', 'montant', 'statut', 'livreur'];

  constructor(private livraisonService: LivraisonService, private userService: UserService) {}

  ngOnInit(): void {
    this.getLivraisons();
    this.getAllLivreurs();
  }

  getLivraisons(): void {
    this.livraisonService.getAllLivraisons().subscribe(
      (data: Livraison[]) => {
        this.livraisons = data;
        console.log('Livraisons chargées:', this.livraisons);
      },
      error => {
        console.error('Erreur lors du chargement des livraisons:', error);
      }
    );
  }

  getAllLivreurs(): void {
    this.userService.getAllLivreurs().subscribe(
      (data: Livreur[]) => {
        this.livreurs = data;
        this.livreursMap = new Map(this.livreurs.map(l => [l.id, l]));
        console.log('Livreurs chargés:', this.livreurs);
      },
      error => {
        console.error('Erreur lors du chargement des livreurs:', error);
      }
    );
  }

  assignerLivreur(livraisonId: number, livreurId: number): void {
    if (!livreurId) {
      return;
    }

    this.livraisonService.assignerLivreur(livraisonId, livreurId).subscribe(
      (result: Livraison) => {
        console.log('Livreur assigné avec succès:', result);
        const index = this.livraisons.findIndex(l => l.id === livraisonId);
        if (index !== -1) {
          this.livraisons[index] = result;
          this.livraisons = [...this.livraisons];
        }
      },
      (error) => {
        console.error('Erreur lors de l\'assignation du livreur:', error);
      }
    );
  }

  onSelectLivreur(livraisonId: number, event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const livreurId = Number(selectElement.value);
    this.assignerLivreur(livraisonId, livreurId);
  }

  getLivreurName(livraison: Livraison): string {
    return livraison.livreur?.username || 'Non assigné';
  }

  isLivreurAssigned(livraison: Livraison): boolean {
    return !!livraison.livreur;
  }

  canAssignLivreur(livraison: Livraison): boolean {
    return livraison.statut === 'EN_COURS';
  }
}
