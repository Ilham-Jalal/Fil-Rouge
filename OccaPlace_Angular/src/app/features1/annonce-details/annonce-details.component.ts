import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {AnnonceResponseDTO} from "../../core/dto/AnnonceResponseDTO";
import {AnnonceService} from "../../core/service/annonce.service";


@Component({
  selector: 'app-annonce-details',
  templateUrl: './annonce-details.component.html',
  styleUrls: ['./annonce-details.component.scss']
})
export class AnnonceDetailsComponent implements OnInit {
  annonce: AnnonceResponseDTO | undefined;

  constructor(
    private route: ActivatedRoute,
    private annonceService: AnnonceService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.annonceService.findById(id).subscribe(
        (data)=>
          this.annonce = data
      )
    }
  }

  backToList(): void {
    this.router.navigate(['/annonces']);
  }
}
