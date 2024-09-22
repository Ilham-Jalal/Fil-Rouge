import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { UserService } from '../../core/service/user-service.service';
import { Router } from '@angular/router';
import { Role } from '../../core/enum/Role';
import { UserDTO } from '../../core/dto/UserDTO';
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgForOf
  ],
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent {
  addUserForm: FormGroup;
  roles = [Role.LIVREUR, Role.ADMIN];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {
    this.addUserForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      role: [Role.USER, Validators.required] // Default to USER role
    });
  }

  onSubmit(): void {
    if (this.addUserForm.valid) {
      const userDTO: UserDTO = this.addUserForm.value;
      const role = userDTO.role; // Extract role from form value
      this.userService.addUserByAdmin(role, userDTO).subscribe(
        () => {
          this.router.navigate(['/']); // Redirect on success
        },
        (error) => {
          console.error('Error adding user:', error);
        }
      );
    }
  }

  public isControlInvalid(controlName: string): boolean {
    const control = this.addUserForm.get(controlName);
    return !!(control && control.invalid && control.dirty);
  }
}
