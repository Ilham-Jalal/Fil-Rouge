import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {MessageCreateDTO} from "../dto/MessageCreateDTO";
import {MessageResponseDTO} from "../dto/MessageResponseDTO";


@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private baseUrl = 'http://localhost:8180/user/api/messages'; // Remplacez par votre URL de base

  constructor(private http: HttpClient) {}

  sendMessage(toUserId: number, message: MessageCreateDTO): Observable<MessageResponseDTO> {
    return this.http.post<MessageResponseDTO>(`${this.baseUrl}/send/${toUserId}`, message, {
      headers: this.getHeaders(),
    });
  }

  replyToMessage(parentMessageId: number, message: MessageCreateDTO): Observable<MessageResponseDTO> {
    return this.http.post<MessageResponseDTO>(`${this.baseUrl}/reply/${parentMessageId}`, message, {
      headers: this.getHeaders(),
    });
  }

  getSentMessages(): Observable<MessageResponseDTO[]> {
    return this.http.get<MessageResponseDTO[]>(`${this.baseUrl}/sent`, {
      headers: this.getHeaders(),
    });
  }

  getReceivedMessages(): Observable<MessageResponseDTO[]> {
    return this.http.get<MessageResponseDTO[]>(`${this.baseUrl}/received`, {
      headers: this.getHeaders(),
    });
  }

  private getHeaders(): HttpHeaders {
    // Ajoutez ici votre logique d'authentification, par exemple, l'ajout d'un token JWT si nécessaire
    const token = localStorage.getItem('token'); // Ou un autre moyen de récupérer votre token
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
  }
}
