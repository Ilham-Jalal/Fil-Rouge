import {Categorie} from "../enum/Categorie";
import {Disponibilite} from "../enum/Disponibilite";


export interface AnnonceResponseDTO {
  id: number;
  title: string;
  description: string;
  price: number;
  category: Categorie;
  disponibilite: Disponibilite;
  creationDate: string;
  vendeurId: number;
  vendeurName: string;
  acheteurId?: number; 
  livraisonId?: number;
}
