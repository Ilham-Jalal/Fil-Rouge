import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentaireDto } from '../dto/CommentaireDto';

@Injectable({
  providedIn: 'root'
})
export class CommentaireService {
  private apiUrl = 'http://localhost:8080/user/api/commentaires';

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

  createCommentaire(commentaireDto: CommentaireDto, annonceId: number): Observable<CommentaireDto> {
    return this.http.post<CommentaireDto>(`${this.apiUrl}/${annonceId}`, commentaireDto);
  }

  updateCommentaire(commentaireId: number | undefined, commentaireDto: CommentaireDto): Observable<CommentaireDto> {
    return this.http.put<CommentaireDto>(`${this.apiUrl}/${commentaireId}`, commentaireDto);
  }

  deleteCommentaire(commentaireId: number | undefined): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${commentaireId}`);
  }

}
