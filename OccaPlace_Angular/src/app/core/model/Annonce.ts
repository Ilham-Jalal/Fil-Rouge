import {Commentaire} from "./Commentaire";
import {Favori} from "./Favori";
import {Livraison} from "./Livraison";
import {Utilisateur} from "./Utilisateur";
import {Categorie} from "../enum/Categorie";
import {Disponibilite} from "../enum/Disponibilite";

export interface Annonce {
  id: number;
  title: string;
  description: string;
  price: number;
  category: Categorie;
  creationDate: Date;
  disponibilite: Disponibilite;
  vendeur?: Utilisateur;
  acheteur?: Utilisateur;
  livraison?: Livraison;
  commentaires?: Commentaire[];
  favoris?: Favori[];
}
