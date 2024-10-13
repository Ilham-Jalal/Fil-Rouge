import { Component, OnInit } from '@angular/core';
import { Livraison } from "../../core/model/Livraison";
import { UserDTO } from "../../core/dto/UserDTO";
import { LivraisonService } from "../../core/service/livraison.service";
import { UserService } from "../../core/service/user-service.service";

@Component({
  selector: 'app-livraison-list',
  templateUrl: './livraison-list.component.html',
  styleUrls: ['./livraison-list.component.css']
})
export class LivraisonListComponent implements OnInit {
  livraisons: Livraison[] = [];
  livreurs: UserDTO[] = [];
  livreursMap: Map<number, UserDTO> = new Map();

  displayedColumns: string[] = ['id', 'adresseVendeur', 'adresseAcheteur', 'montant', 'statut', 'livreur'];

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
      (data: UserDTO[]) => {
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
        // Mettre à jour la livraison dans le tableau
        const index = this.livraisons.findIndex(l => l.id === livraisonId);
        if (index !== -1) {
          this.livraisons[index] = result; // Mettre à jour la livraison
          this.livraisons = [...this.livraisons]; // Forcer la mise à jour de la vue
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
    if (livraison.livreur?.id && this.livreursMap.has(livraison.livreur.id)) {
      return this.livreursMap.get(livraison.livreur.id)!.username;
    }
    return 'Non assigné';
  }

  isLivreurAssigned(livraison: Livraison): boolean {
    return !!livraison.livreur; // Vérifie si un livreur est assigné
  }

  canAssignLivreur(livraison: Livraison): boolean {
    return livraison.statut === 'EN_COURS'; // Vérifie si le statut de la livraison permet l'assignation d'un livreur
  }
}
