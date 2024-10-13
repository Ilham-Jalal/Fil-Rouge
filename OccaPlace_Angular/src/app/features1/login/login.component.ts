import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/service/auth-service.service';
import { UserService } from '../../core/service/user-service.service';
import { LoginRequest } from '../../core/dto/LoginRequest';
import { SignUpRequest } from '../../core/dto/SignUpRequest';
import { NgClass, NgIf } from "@angular/common";
import { Role } from '../../core/enum/Role';
import { MatButton } from "@angular/material/button";
import { MatCard, MatCardTitle } from "@angular/material/card";
import { MatFormField, MatLabel } from "@angular/material/form-field";
import { MatInput } from "@angular/material/input";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    MatCard,
    MatButton,
    MatFormField,
    MatInput,
    MatLabel,
    MatCardTitle,
    NgClass
  ],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  signUpForm!: FormGroup;
  isLoginMode: boolean = true;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initLoginForm();
    this.initSignUpForm();
  }

  initLoginForm(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  initSignUpForm(): void {
    this.signUpForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    });
  }

  switchMode(): void {
    this.isLoginMode = !this.isLoginMode;
  }

  onSubmit(): void {
    if (this.isLoginMode) {
      this.login();
    } else {
      this.signUp();
    }
  }

  login(): void {
    if (this.loginForm.invalid) {
      this.errorMessage = 'Veuillez remplir correctement tous les champs obligatoires.';
      return;
    }

    const loginRequest: LoginRequest = this.loginForm.value;

    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        const { token, role } = response;
        localStorage.setItem('jwt', token);
        this.redirectUserBasedOnRole(role);
      },
      error: () => {
        this.errorMessage = 'Échec de la connexion. Vérifiez vos identifiants.';
      }
    });
  }

  signUp(): void {
    if (this.signUpForm.invalid) {
      this.errorMessage = 'Veuillez remplir correctement tous les champs obligatoires.';
      return;
    }

    const signUpRequest: SignUpRequest = this.signUpForm.value;
    if (signUpRequest.password !== this.signUpForm.value.confirmPassword) {
      this.errorMessage = 'Les mots de passe ne correspondent pas.';
      return;
    }

    this.userService.signUpUser(signUpRequest).subscribe({
      next: () => {
        this.isLoginMode = true;
        this.errorMessage = null;
      },
      error: () => {
        this.errorMessage = 'Échec de l\'inscription. Veuillez réessayer.';
      }
    });
  }

  redirectUserBasedOnRole(role: string): void {
    switch (role) {
      case Role.ADMIN:
        this.router.navigate(['/dashboard/main']);
        break;
      case Role.USER:
        this.router.navigate(['/annonces']);
        break;
      case Role.LIVREUR:
        this.router.navigate(['/livreurDashboard']);
        break;
      default:
        this.router.navigate(['/access-denied']);
        break;
    }
  }
}
