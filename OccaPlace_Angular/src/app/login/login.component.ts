import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth-service.service';
import { LoginRequest } from '../dto/LoginRequest';
import { NgIf } from "@angular/common";
import { Role } from '../enum/Role';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    const loginRequest: LoginRequest = this.loginForm.value;

    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        const { token, role } = response;
        console.log('Received token:', token);
        console.log('Received role:', role);
        localStorage.setItem('jwt', token);
        switch (role) {
          case Role.ADMIN:
            console.log("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            this.router.navigate(['/add-user']);
            break;
          case Role.USER:
            this.router.navigate(['/user/user']);
            break;
          case Role.LIVREUR:
            this.router.navigate(['/technician/technician']);
            break;
          default:
            console.log('Unknown role:', role);
            this.router.navigate(['/access-denied']);
            break;
        }
      },
      error: (err) => {
        console.error('Login error:', err);
        this.errorMessage = 'Invalid username or password';
      }
    });
  }
}
