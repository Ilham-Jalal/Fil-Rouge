import {User} from "./User";
import {Utilisateur} from "./Utilisateur";
import {Livraison} from "./Livraison";



export class Livreur extends User {
  deliveryZone: string;
  livraisons?: Livraison[];
  utilisateur?: Utilisateur;
}
