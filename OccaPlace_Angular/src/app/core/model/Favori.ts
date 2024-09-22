import {Utilisateur} from "./Utilisateur";
import {Annonce} from "./Annonce";

export interface Favori {
  id: number;
  dateAjout: Date;
  utilisateur?: Utilisateur;
  annonce?: Annonce;
}
