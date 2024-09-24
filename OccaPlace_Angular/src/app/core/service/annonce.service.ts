import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
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
  private apiUrl = 'http://localhost:8180/user/api/annonces';

  constructor(private http: HttpClient) { }

  createAnnonceWithImages(annonceDTO: AnnonceCreateDTO, attachments: File[]): Observable<AnnonceCreateDTO> {
    const formData = new FormData();
    formData.append('annonce', new Blob([JSON.stringify(annonceDTO)], { type: 'application/json' }));
    attachments.forEach((file, index) => formData.append('attachments', file, file.name));

    return this.http.post<Annonce>(`${this.apiUrl}/create`, formData);
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
