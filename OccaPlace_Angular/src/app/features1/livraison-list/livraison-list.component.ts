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

  displayedColumns: string[] = ['id', 'adresseVendeur', 'adresseAcheteur', 'montant', 'statut', 'action'];

  constructor(private livraisonService: LivraisonService, private userService: UserService) {}

  ngOnInit(): void {
    this.getLivraisons();
    this.getAllLivreurs();
  }

  getLivraisons(): void {
    this.livraisonService.getAllLivraisons().subscribe((data: Livraison[]) => {
      this.livraisons = data;
    });
  }

  getAllLivreurs(): void {
    this.userService.getAllLivreurs().subscribe((data: UserDTO[]) => {
      this.livreurs = data;
    });
  }

  assignerLivreur(livraisonId: number, livreurId: number): void {
    if (!livreurId) {
      return;
    }

    this.livraisonService.assignerLivreur(livraisonId, livreurId).subscribe(
      (result) => {
        console.log('Livreur assigné avec succès:', result);
        this.getLivraisons();
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
}
