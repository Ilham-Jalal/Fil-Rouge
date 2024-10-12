import {Categorie} from "../enum/Categorie";
import {Disponibilite} from "../enum/Disponibilite";
import {StatutLivraison} from "../enum/StatutLivraison";


export interface AnnonceResponseDTO {
  id: number;
  title: string;
  description: string;
  price: number;
  category: Categorie;
  disponibilite: Disponibilite;
  creationDate: string;
  vendeurId: number;
  vendeurName?: string;
  vendeurEmail: string;
  acheteurId?: number;
  livraisonId?: number;
  statutLivraison?: StatutLivraison;
  images: string[];
}
