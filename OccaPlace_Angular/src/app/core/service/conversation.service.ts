import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conversation } from '../model/Conversation';
import { MessageResponseDTO } from '../dto/MessageResponseDTO';

@Injectable({
  providedIn: 'root'
})
export class ConversationService {
  private apiUrl = 'http://localhost:8180/user/api';

  constructor(private http: HttpClient) {}

  getConversation(id: number): Observable<Conversation> {
    return this.http.get<Conversation>(`${this.apiUrl}/conversations/${id}`);
  }

  getMessages(conversationId: number): Observable<MessageResponseDTO[]> {
    return this.http.get<MessageResponseDTO[]>(`${this.apiUrl}/conversations/${conversationId}/messages`);
  }

  sendMessage(conversationId: number, message: { toUserId: number; content: string }): Observable<MessageResponseDTO> {
    return this.http.post<MessageResponseDTO>(`${this.apiUrl}/conversations/${conversationId}/messages`, message);
  }

  deleteMessage(messageId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/messages/${messageId}`);
  }

  updateMessage(messageId: number, content: string): Observable<MessageResponseDTO> {
    return this.http.put<MessageResponseDTO>(`${this.apiUrl}/messages/${messageId}`, { content });
  }
}
