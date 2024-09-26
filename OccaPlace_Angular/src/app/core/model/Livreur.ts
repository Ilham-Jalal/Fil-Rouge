import {User} from "./User";
import {Utilisateur} from "./Utilisateur";
import {Livraison} from "./Livraison";



export class Livreur extends User {
  livraisons?: Livraison[];
  utilisateur?: Utilisateur;
}
