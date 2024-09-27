import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {AnnonceResponseDTO} from "../dto/AnnonceResponseDTO";
import {AnnonceCreateDTO} from "../dto/AnnonceCreateDTO";
import {AnnonceUpdateDTO} from "../dto/AnnonceUpdateDTO";
import {Categorie} from "../enum/Categorie";
import {Disponibilite} from "../enum/Disponibilite";
import {Annonce} from "../model/Annonce";
@Injectable({
  providedIn: 'root'
})
export class AnnonceService {
  private apiUrl = 'http://localhost:8181/user/api/annonces';

  constructor(private http: HttpClient) { }

  createAnnonceWithImages(annonceDTO: AnnonceUpdateDTO, attachments: File[]): Observable<AnnonceCreateDTO> {
    const formData = new FormData();
    formData.append('annonce', new Blob([JSON.stringify(annonceDTO)], { type: 'application/json' }));
    attachments.forEach((file) => formData.append('images', file, file.name)); // Modification ici

    return this.http.post<Annonce>(`${this.apiUrl}/create`, formData).pipe(
      catchError(error => {
        console.error('Erreur lors de l\'appel API:', error);
        return throwError(error);
      })
    );
  }

  getAllAnnonces(): Observable<AnnonceResponseDTO[]> {
    return this.http.get<AnnonceResponseDTO[]>(this.apiUrl);
  }

  updateAnnonce(id: number, updatedAnnonceDTO: AnnonceUpdateDTO): Observable<AnnonceResponseDTO> {
    return this.http.put<AnnonceResponseDTO>(`${this.apiUrl}/${id}`, updatedAnnonceDTO);
  }

  deleteAnnonce(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getAnnoncesByCategory(category: Categorie): Observable<AnnonceResponseDTO[]> {
    return this.http.get<AnnonceResponseDTO[]>(`${this.apiUrl}/category/${category}`);
  }

  getAnnoncesByDisponibilite(disponibilite: Disponibilite): Observable<AnnonceResponseDTO[]> {
    return this.http.get<AnnonceResponseDTO[]>(`${this.apiUrl}/disponibilite/${disponibilite}`);
  }

  searchAnnonces(title: string, description: string, category: Categorie, priceMin: number, priceMax: number): Observable<AnnonceResponseDTO[]> {
    return this.http.get<AnnonceResponseDTO[]>(`${this.apiUrl}/search`, {
      params: {
        title,
        description,
        category,
        priceMin: priceMin.toString(),
        priceMax: priceMax.toString()
      }
    });
  }
}
