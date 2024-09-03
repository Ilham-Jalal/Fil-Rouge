import {Annonce} from "./Annonce";
import {Utilisateur} from "./Utilisateur";

export interface Commentaire {
  id: number;
  contenu: string;
  dateCreation: Date;
  utilisateur?: Utilisateur;
  annonce?: Annonce;
}
