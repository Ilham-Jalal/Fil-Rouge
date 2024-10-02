import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Message} from "../model/Message";

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private baseUrl = 'http://localhost:8180/user/api/messages';

  constructor(private http: HttpClient) {}

  sendMessage(toUserId: number, content: string): Observable<Message> {
    return this.http.post<Message>(`${this.baseUrl}/send/${toUserId}`, content);
  }

  replyToMessage(parentMessageId: number, content: string): Observable<Message> {
    return this.http.post<Message>(`${this.baseUrl}/reply/${parentMessageId}`, content);
  }

  getSentMessages(): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.baseUrl}/sent`);
  }

  getReceivedMessages(): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.baseUrl}/received`);
  }
}
