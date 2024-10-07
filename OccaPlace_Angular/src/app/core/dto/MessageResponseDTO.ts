export interface MessageResponseDTO {
  id: number;
  content: string;
  timestamp: string;
  fromUserName: string;
  toUserName: string;
  parentMessageId: number | null;
}
