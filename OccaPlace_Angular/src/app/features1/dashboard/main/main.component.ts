import { Component, OnInit } from '@angular/core';
import { Categorie } from "../../../core/enum/Categorie";
import { AnnonceService } from "../../../core/service/annonce.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  totalAnnonces: number = 0;
  totalByCategory: { [key: string]: number } = {
    [Categorie.VETEMENTS]: 0,
    [Categorie.AUTOMOBILE]: 0,
    [Categorie.ELECTRONIQUE]: 0,
    [Categorie.IMMOBILIER]: 0,
    [Categorie.MEUBLES]: 0,
    [Categorie.AUTRE]: 0,
  };

  constructor(private annonceService: AnnonceService) {}

  ngOnInit(): void {
    this.loadTotals();
  }

  loadTotals(): void {
    this.annonceService.countTotalAnnonces().subscribe(total => {
      this.totalAnnonces = total;
    });

    // Count totals per category
    for (const category of Object.values(Categorie)) {
      this.annonceService.countAnnoncesByCategory(category).subscribe(count => {
        this.totalByCategory[category] = count;
      });
    }
  }

  getCategories(): string[] {
    return Object.values(Categorie);
  }
}
