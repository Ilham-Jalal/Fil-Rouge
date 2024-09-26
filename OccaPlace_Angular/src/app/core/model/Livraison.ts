import {Annonce} from "./Annonce";
import {Livreur} from "./Livreur";
import {Utilisateur} from "./Utilisateur";
import {StatutLivraison} from "../enum/StatutLivraison";

export interface Livraison {
  id: number;
  adresse: string;
  montant: number;
  statut: StatutLivraison;
  dateLivraison: Date;
  utilisateur?: Utilisateur;  // Optional to handle @JsonIgnore
  livreur?: Livreur;  // Optional to handle @JsonIgnore
  annonces?: Annonce[];  // Optional to handle @JsonIgnore
}
