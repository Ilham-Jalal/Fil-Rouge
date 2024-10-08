import { Component, OnInit } from '@angular/core';
import { Categorie } from "../../../core/enum/Categorie";
import { AnnonceService } from "../../../core/service/annonce.service";
import { UserService } from "../../../core/service/user-service.service";
import { Chart, registerables } from 'chart.js';
import { AnnonceResponseDTO } from "../../../core/dto/AnnonceResponseDTO";

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
  totalByCategory: { [key: string]: number } = {
    [Categorie.VETEMENTS]: 0,
    [Categorie.AUTOMOBILE]: 0,
    [Categorie.ELECTRONIQUE]: 0,
    [Categorie.IMMOBILIER]: 0,
    [Categorie.MEUBLES]: 0,
    [Categorie.AUTRE]: 0,
  };
  statistics: { title: string; value: number; unit: string; color: string; icon: string; }[] = [];
  chart: any;

  // Ajout des variables pour le carousel d'images
  currentImageIndex: number = 0;

  constructor(private annonceService: AnnonceService, private userService: UserService) {}

  ngOnInit(): void {
    this.loadTotals();
    this.loadTotalUsers();
    this.loadTotalLivreurs();
    this.loadAnnonces();

    // Démarrer le carousel des images
    setInterval(() => {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.annonces.length;
    }, 3000); // Change l'image toutes les 3 secondes
  }

  loadAnnonces(): void {
    this.annonceService.getAllAnnonces().subscribe((data: AnnonceResponseDTO[]) => {
      this.annonces = data;
    });
  }

  loadTotalUsers(): void {
    this.userService.countTotalUsers().subscribe(total => {
      this.totalUsers = total;
      this.updateStatistics();
      this.checkAndCreateChart();
    });
  }

  loadTotalLivreurs(): void {
    this.userService.countTotalLivreurs().subscribe(total => {
      this.totalLivreurs = total;
      this.updateStatistics();
      this.checkAndCreateChart();
    });
  }

  loadTotals(): void {
    this.annonceService.countTotalAnnonces().subscribe(total => {
      this.totalAnnonces = total;
      this.updateStatistics();
      this.checkAndCreateChart();
    });

    for (const category of Object.values(Categorie)) {
      this.annonceService.countAnnoncesByCategory(category).subscribe(count => {
        this.totalByCategory[category] = count;
        this.updateStatistics();
        this.checkAndCreateChart();
      });
    }
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

  private updateStatistics(): void {
    this.statistics = [
      { title: 'Total Annonces', value: this.totalAnnonces, unit: 'annonces', color: 'primary', icon: 'ni ni-money-coins' },
      { title: 'Total Users', value: this.totalUsers, unit: 'utilisateurs', color: 'warning', icon: 'ni ni-circle-08' },
      ...Object.keys(this.totalByCategory).map(category => ({
        title: `Total ${category}`,
        value: this.totalByCategory[category],
        unit: 'annonces',
        color: 'success',
        icon: 'ni ni-paper-diploma',
      }))
    ];
  }

  private checkAndCreateChart(): void {
    if (this.totalAnnonces > 0 && Object.values(this.totalByCategory).every(count => count >= 0)) {
      this.createChart();
    }
  }
}
