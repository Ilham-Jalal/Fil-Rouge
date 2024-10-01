import {Component, EventEmitter, Output} from '@angular/core';
import {Categorie} from "../../core/enum/Categorie";
import {AnnonceService} from "../../core/service/annonce.service";
import {AnnonceResponseDTO} from "../../core/dto/AnnonceResponseDTO";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-header2',
  templateUrl: './header2.component.html',
  standalone: true,
  imports: [
    FormsModule
  ],
  styleUrl: './header2.component.css'
})
export class Header2Component {

}
