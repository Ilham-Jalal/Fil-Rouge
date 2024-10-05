import {Component, EventEmitter, Output} from '@angular/core';
import {Categorie} from "../../core/enum/Categorie";
import {AnnonceService} from "../../core/service/annonce.service";
import {AnnonceResponseDTO} from "../../core/dto/AnnonceResponseDTO";
import {FormsModule} from "@angular/forms";
import {NgOptimizedImage} from "@angular/common";
import {MatToolbar} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";
import {AuthService} from "../../core/service/auth-service.service";
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-header2',
  templateUrl: './header2.component.html',
  standalone: true,
  imports: [
    FormsModule,
    NgOptimizedImage,
    MatToolbar,
    MatIcon,
    MatButton,
    RouterLink
  ],
  styleUrl: './header2.component.css'
})
export class Header2Component {
  constructor(private authService: AuthService, private router: Router) {}

  onLogout() {
    this.authService.logout();  // Call logout from AuthService
    this.router.navigate(['/login']);  // Redirect to the login page after logout
  }
}
