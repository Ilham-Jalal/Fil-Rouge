import { Component, OnInit } from '@angular/core';
import { AnnonceService } from "../../../core/service/annonce.service";
import { UserService } from "../../../core/service/user-service.service";
import { AnnonceResponseDTO } from "../../../core/dto/AnnonceResponseDTO";
import { UserDTO } from "../../../core/dto/UserDTO";
import { Chart, registerables } from 'chart.js';
import {MatDialog} from "@angular/material/dialog";
import {AddLivreurDialogComponent} from "../../add-livreur-dialog/add-livreur-dialog.component";
import {Role} from "../../../core/enum/Role";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  totalUsers: number = 0;
  totalLivreurs: number = 0;
  totalAnnonces: number = 0;
  annonces: AnnonceResponseDTO[] = [];
  users: UserDTO[] = [];
  livreurs: UserDTO[] = [];
  displayedColumns: string[] = ['username', 'email'];
  totalByCategory: { [key: string]: number } = {};
  currentImageIndex: number = 0;
  chart: any;

  constructor(private annonceService: AnnonceService, private userService: UserService,private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadTotals();
    this.loadTotalUsers();
    this.loadTotalLivreurs();
    this.loadAnnonces();
    this.loadUserList();
    this.loadlivreurList();

    setInterval(() => {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.annonces.length;
    }, 3000);
  }

  loadAnnonces(): void {
    this.annonceService.getAllAnnonces().subscribe((data: AnnonceResponseDTO[]) => {
      this.annonces = data;
      this.calculateTotalByCategory();
      this.checkAndCreateChart();
    });
  }

  loadTotalUsers(): void {
    this.userService.countTotalUsers().subscribe(total => {
      this.totalUsers = total;
      this.checkAndCreateChart();
    });
  }

  loadTotalLivreurs(): void {
    this.userService.countTotalLivreurs().subscribe(total => {
      this.totalLivreurs = total;
      this.checkAndCreateChart();
    });
  }

  loadTotals(): void {
    this.annonceService.countTotalAnnonces().subscribe(total => {
      this.totalAnnonces = total;
      this.checkAndCreateChart();
    });
  }

  loadUserList(): void {
    this.userService.getAllUsers().subscribe((users: UserDTO[]) => {
      this.users = users;
    });
  }
  loadlivreurList(): void {
    this.userService.getAllLivreurs().subscribe((livreurs: UserDTO[]) => {
      this.livreurs = livreurs;
    });
  }

  calculateTotalByCategory(): void {
    this.totalByCategory = {};
    this.annonces.forEach(annonce => {
      const category = annonce.category;
      if (!this.totalByCategory[category]) {
        this.totalByCategory[category] = 0;
      }
      this.totalByCategory[category]++;
    });
  }

  createChart(): void {
    if (this.chart) {
      this.chart.destroy();
    }

    Chart.register(...registerables);
    const ctx = document.getElementById('categoryChart') as HTMLCanvasElement;

    this.chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: Object.keys(this.totalByCategory),
        datasets: [{
          label: 'Annonces par Catégorie',
          data: Object.values(this.totalByCategory),
          backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)'
          ],
          borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)'
          ],
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }

  openAddLivreurDialog(): void {
    const dialogRef = this.dialog.open(AddLivreurDialogComponent, {
      width: '300px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.addNewLivreur(result);
      }
    });
  }

  addNewLivreur(newLivreur: UserDTO): void {
    this.userService.addUserByAdmin(Role.LIVREUR, newLivreur).subscribe(
      (response) => {
        console.log('New livreur added:', response);
        this.loadlivreurList(); // Refresh the livreur list
        this.loadTotalLivreurs(); // Update the total livreurs count
      },
      (error) => {
        console.error('Error adding new livreur:', error);
        // Handle error (e.g., show error message to user)
      }
    );
  }

  private checkAndCreateChart(): void {
    if (this.totalAnnonces > 0 && this.totalUsers > 0 && this.totalLivreurs > 0) {
      this.createChart();
    }
  }
}
