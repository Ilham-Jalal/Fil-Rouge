import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentaireDto } from '../dto/CommentaireDto';
import {Commentaire} from "../model/Commentaire";

@Injectable({
  providedIn: 'root'
})
export class CommentaireService {
  private apiUrl = 'http://localhost:8180/user/api/commentaires';

  constructor(private http: HttpClient) {}

  getAllCommentaires(): Observable<CommentaireDto[]> {
    return this.http.get<CommentaireDto[]>(`${this.apiUrl}`);
  }

  getCommentaireById(id: number): Observable<CommentaireDto> {
    return this.http.get<CommentaireDto>(`${this.apiUrl}/${id}`);
  }

  getCommentairesByAnnonce(annonceId: number): Observable<CommentaireDto[]> {
    return this.http.get<CommentaireDto[]>(`${this.apiUrl}/annonce/${annonceId}`);
  }

  createCommentaire(commentaireDto: { contenu: string }, annonceId: number | undefined): Observable<CommentaireDto> {
    return this.http.post<CommentaireDto>(`${this.apiUrl}/${annonceId}`, commentaireDto);
  }

  updateCommentaire(id: number, commentaire: CommentaireDto): Observable<CommentaireDto> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.put<CommentaireDto>(url, commentaire);
  }

  deleteCommentaire(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }

}
