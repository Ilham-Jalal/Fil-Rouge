import {Message} from "./Message";

export interface Conversation {
  id: number;
  vendeur: {
    id: number;
    username: string;
  };
  acheteur: {
    id: number;
    username: string;
  };
  messages: Message[];
}
