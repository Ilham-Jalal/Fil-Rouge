import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AnnonceUpdateDTO } from "../../core/dto/AnnonceUpdateDTO";
import {Disponibilite} from "../../core/enum/Disponibilite";

@Component({
  selector: 'app-update-annonce-dialog',
  templateUrl: './update-annonce-dialog.component.html',
  styleUrls: ['./update-annonce-dialog.component.css']
})
export class UpdateAnnonceDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<UpdateAnnonceDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: AnnonceUpdateDTO
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  updateAnnonce(): void {
    // Pass the updated data to the parent component
    this.dialogRef.close(this.data);
  }

  protected readonly Disponibilite = Disponibilite;
}
