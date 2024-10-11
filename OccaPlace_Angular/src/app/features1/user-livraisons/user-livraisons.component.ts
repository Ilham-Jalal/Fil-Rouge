import { Component, OnInit } from '@angular/core';
import { Livraison } from "../../core/model/Livraison";
import { LivraisonService } from "../../core/service/livraison.service";

@Component({
  selector: 'app-user-livraisons',
  templateUrl: './user-livraisons.component.html',
  styleUrls: ['./user-livraisons.component.scss']
})
export class UserLivraisonsComponent implements OnInit {
  livraisons: Livraison[] = [];

  constructor(private livraisonService: LivraisonService) {}

  ngOnInit(): void {
    this.getUserLivraisons();
  }

  getUserLivraisons(): void {
    this.livraisonService.getUserLivraisons().subscribe((data: Livraison[]) => {
      this.livraisons = data;
    });
  }
}
