import {Annonce} from "./Annonce";
import {User} from "./User";
import {Favori} from "./Favori";
import {Commentaire} from "./Commentaire";
import {Message} from "./Message";
import {Livraison} from "./Livraison";

export class Utilisateur extends User {
  ventes?: Annonce[];
  achats?: Annonce[];
  sentMessages?: Message[];
  receivedMessages?: Message[];
  livraisons?: Livraison[];
  commentaires?: Commentaire[];
  favoris?: Favori[];
}
