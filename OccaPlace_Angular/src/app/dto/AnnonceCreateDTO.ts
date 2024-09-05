import {Disponibilite} from "../enum/Disponibilite";
import {Categorie} from "../enum/Categorie";

export interface AnnonceCreateDTO {
  title: string;
  description: string;
  price: number;
  category: Categorie;
  disponibilite: Disponibilite;
}
