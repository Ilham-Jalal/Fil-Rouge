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
  private apiUrl = 'http://localhost:8180/user/api/annonces';
  private adminApiUrl ='http://localhost:8180/admin'
  private url ='http://localhost:8180/annonces';
  constructor(private http: HttpClient) { }

  createAnnonceWithImages(annonceDTO: AnnonceUpdateDTO, attachments: File[]): Observable<AnnonceResponseDTO> {
    const formData = new FormData();
    formData.append('annonce', new Blob([JSON.stringify(annonceDTO)], { type: 'application/json' }));
    attachments.forEach((file) => formData.append('images', file, file.name));

    return this.http.post<AnnonceResponseDTO>(`${this.apiUrl}/create`, formData).pipe(
      catchError(error => {
        console.error('Erreur lors de l\'appel API:', error);
        return throwError(error);
      })
    );
  }

  findById(id: number): Observable<AnnonceResponseDTO> {
    return this.http.get<AnnonceResponseDTO>(`${this.apiUrl}/${id}`);
  }
  getAllAnnonces(): Observable<AnnonceResponseDTO[]> {
    return this.http.get<AnnonceResponseDTO[]>(this.url);
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


  searchAnnonces(titleOrDescription: string, category: Categorie, priceMin: number, priceMax: number): Observable<AnnonceResponseDTO[]> {
    console.log('Recherche avec les paramètres :', { titleOrDescription, category, priceMin, priceMax });
    return this.http.get<AnnonceResponseDTO[]>(`${this.apiUrl}/search`, {
      params: {
        title: titleOrDescription,
        description: titleOrDescription,
        category: category,
        minPrice: priceMin.toString(),
        maxPrice: priceMax.toString()
      }
    });
  }


  countTotalAnnonces(): Observable<number> {
    return this.http.get<number>(`${this.adminApiUrl}/count`).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération du nombre total d\'annonces:', error);
        return throwError(error);
      })
    );
  }

  countAnnoncesByCategory(category: string): Observable<number> {
    return this.http.get<number>(`${this.adminApiUrl}/count/category/${category}`).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération du nombre d\'annonces par catégorie:', error);
        return throwError(error);
      })
    );
  }
}
