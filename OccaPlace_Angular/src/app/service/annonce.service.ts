import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AnnonceResponseDTO} from "../dto/AnnonceResponseDTO";
import {AnnonceCreateDTO} from "../dto/AnnonceCreateDTO";
import {AnnonceUpdateDTO} from "../dto/AnnonceUpdateDTO";
import {Categorie} from "../enum/Categorie";
import {Disponibilite} from "../enum/Disponibilite";

@Injectable({
  providedIn: 'root'
})
export class AnnonceService {

  private apiUrl = 'http://localhost:8080/user/api/annonces';

  constructor(private http: HttpClient) { }

  createAnnonce(annonceDTO: AnnonceCreateDTO): Observable<AnnonceResponseDTO> {
    return this.http.post<AnnonceResponseDTO>(this.apiUrl, annonceDTO);
  }

  getAnnoncesByUser(): Observable<AnnonceResponseDTO[]> {
    return this.http.get<AnnonceResponseDTO[]>(`${this.apiUrl}/annonces`);
  }

  updateAnnonce(id: number, updatedAnnonceDTO: AnnonceUpdateDTO): Observable<AnnonceResponseDTO> {
    return this.http.put<AnnonceResponseDTO>(`${this.apiUrl}/${id}`, updatedAnnonceDTO);
  }

  deleteAnnonce(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getAllAnnonces(): Observable<AnnonceResponseDTO[]> {
    return this.http.get<AnnonceResponseDTO[]>(this.apiUrl);
  }

  getAnnoncesByCategory(category: Categorie): Observable<AnnonceResponseDTO[]> {
    return this.http.get<AnnonceResponseDTO[]>(`${this.apiUrl}/category/${category}`);
  }

  getAnnoncesByDisponibilite(disponibilite: Disponibilite): Observable<AnnonceResponseDTO[]> {
    return this.http.get<AnnonceResponseDTO[]>(`${this.apiUrl}/disponibilite/${disponibilite}`);
  }

  searchAnnonces(title?: string, description?: string, category?: Categorie, minPrice?: number, maxPrice?: number): Observable<AnnonceResponseDTO[]> {
    let params = new HttpParams();
    if (title) params = params.set('title', title);
    if (description) params = params.set('description', description);
    if (category) params = params.set('category', category);
    if (minPrice != null) params = params.set('minPrice', minPrice);
    if (maxPrice != null) params = params.set('maxPrice', maxPrice);

    return this.http.get<AnnonceResponseDTO[]>(`${this.apiUrl}/search`, { params });
  }
}
