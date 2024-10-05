import { Component, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AnnonceDialogComponent } from '../annonce-dialog/annonce-dialog.component';
import {AnnonceResponseDTO} from "../../core/dto/AnnonceResponseDTO";

@Component({
  selector: 'app-hero2',
  templateUrl: './hero2.component.html',
  styleUrls: ['./hero2.component.css']
})
export class Hero2Component {
  @Output() annonceAdded: EventEmitter<AnnonceResponseDTO> = new EventEmitter<AnnonceResponseDTO>();

  constructor(public dialog: MatDialog) {}

  toggleAnnonceForm(): void {
    const dialogRef = this.dialog.open(AnnonceDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.annonceAdded.emit(result); // Émet l'annonce ajoutée
      }
    });
  }
}
