
import {FormsModule} from "@angular/forms";
import {NgOptimizedImage} from "@angular/common";
import {MatToolbar} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
import {MatButton, MatIconAnchor} from "@angular/material/button";
import {AuthService} from "../../core/service/auth-service.service";
import {Router, RouterLink} from "@angular/router";
import {Component} from "@angular/core";

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
    RouterLink,
    MatIconAnchor
  ],
  styleUrl: './header2.component.css'
})
export class Header2Component {
  showMenu = false;

  constructor(private authService: AuthService, private router: Router) {}

  toggleMenu() {
    this.showMenu = !this.showMenu;
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
