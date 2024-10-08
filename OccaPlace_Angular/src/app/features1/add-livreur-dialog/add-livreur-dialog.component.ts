import {Component, OnInit} from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import {Role} from "../../core/enum/Role";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserDTO} from "../../core/dto/UserDTO";

@Component({
  selector: 'app-add-livreur-dialog',
  templateUrl:'add-livreur-dialog.component.html',
  styleUrl:'add-livreur-dialog.component.css'
})
export class AddLivreurDialogComponent implements OnInit{
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<AddLivreurDialogComponent>
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSave(): void {
    if (this.form.valid) {
      const newLivreur: UserDTO = {
        ...this.form.value,
        role: Role.LIVREUR
      };
      this.dialogRef.close(newLivreur);
    }
  }
}
