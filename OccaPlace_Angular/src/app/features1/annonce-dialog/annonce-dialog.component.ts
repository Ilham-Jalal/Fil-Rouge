import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Disponibilite} from "../../core/enum/Disponibilite";
import {Categorie} from "../../core/enum/Categorie";
import {AnnonceCreateDTO} from "../../core/dto/AnnonceCreateDTO";
import {AnnonceService} from "../../core/service/annonce.service";

@Component({
  selector: 'app-annonce-dialog',
  templateUrl: './annonce-dialog.component.html',
  styleUrls: ['./annonce-dialog.component.scss']
})
export class AnnonceDialogComponent {
  form: FormGroup;
  selectedFiles: File[] = []; // Pour stocker les fichiers sélectionnés

  // Disponibilités et catégories pour les sélecteurs
  disponibilites = Object.values(Disponibilite);
  categories = Object.values(Categorie);

  constructor(
    private annonceService:AnnonceService,
    private dialogRef: MatDialogRef<AnnonceDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder // Injection du FormBuilder
  ) {
    // Initialisation du formulaire
    this.form = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', Validators.required],
      category: ['', Validators.required],
      disponibilite: ['', Validators.required], // Ajout du champ de disponibilité
      images: [null] // Champ pour les images
    });

    // Si des données sont passées au dialogue, préremplissez le formulaire
    if (this.data) {
      this.form.patchValue(this.data);
    }
  }

  // Méthode pour gérer le changement de fichier
  onFileChange(event: any): void {
    const files: FileList = event.target.files;
    this.selectedFiles = Array.from(files); // Convertir FileList en tableau
  }

  // Méthode pour fermer le dialogue et renvoyer les données
  onSave(): void {
    if (this.form.valid) {
      const annonce: AnnonceCreateDTO = this.form.value;

      // Appelez le service pour créer l'annonce avec les images
      this.annonceService.createAnnonceWithImages(annonce, this.selectedFiles).subscribe({
        next: (response) => {
          console.log('Annonce créée avec succès :', response);
          // Vous pouvez ajouter ici un message de succès ou fermer le dialogue
          this.dialogRef.close(response); // Fermer le dialogue et renvoyer la réponse
        },
        error: (error) => {
          console.error('Erreur lors de la création de l\'annonce :', error);
          // Vous pouvez ajouter ici un message d'erreur
        }
      });

      // Réinitialiser le formulaire après l'envoi
      this.form.reset();
    }
  }


  // Méthode pour fermer le dialogue sans enregistrer
  onCancel(): void {
    this.dialogRef.close();
  }
}
