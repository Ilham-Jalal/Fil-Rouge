import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { StatutLivraison } from "../../core/enum/StatutLivraison";

@Component({
  selector: 'app-livraison-dialog',
  templateUrl: './livraison-dialog.component.html',
  styleUrls: ['./livraison-dialog.component.css']
})
export class LivraisonDialogComponent {
  livraisonData: any = {
    statut: StatutLivraison.EN_COURS,
    adresseVendeur: '',
    telephoneVendeur: '',
    adresseAcheteur: '',
    telephoneAcheteur: '',
    montant: 0,
    dateLivraison: new Date()
  };
  statuts = Object.values(StatutLivraison);

  constructor(public dialogRef: MatDialogRef<LivraisonDialogComponent>) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    console.log('Données de livraison soumises:', this.livraisonData);
    this.dialogRef.close(this.livraisonData);
  }
}
