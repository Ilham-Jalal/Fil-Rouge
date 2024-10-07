// message.model.ts
import { Utilisateur } from './Utilisateur';

export interface Message {
  id: number;
  content: string;
  timestamp: Date; // Keep as Date for processing
  fromUser?: Utilisateur;
  toUser?: Utilisateur;
  parentMessage?: Message;
  status?: 'SENDING' | 'SENT' | 'FAILED';
}
