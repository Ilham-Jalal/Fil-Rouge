import {Annonce} from "./Annonce";
import {Livreur} from "./Livreur";
import {Utilisateur} from "./Utilisateur";
import {StatutLivraison} from "../enum/StatutLivraison";

export interface Livraison {
  id: number;
  adresseVendeur: string;
  adresseAcheteur: string;
  montant: number;
  statut: StatutLivraison;
  dateLivraison: Date;
  utilisateur?: Utilisateur;
  livreur?: Livreur;
  annonces?: Annonce[];
}
