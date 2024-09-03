import {Utilisateur} from "./Utilisateur";

export interface Message {
  id: number;
  content: string;
  timestamp: Date;
  fromUser?: Utilisateur;
  toUser?: Utilisateur;
  parentMessage?: Message; 
}
