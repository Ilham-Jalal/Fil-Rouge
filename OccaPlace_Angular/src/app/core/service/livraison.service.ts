import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Livraison} from "../model/Livraison";
import {Annonce} from "../model/Annonce";
import {StatutLivraison} from "../enum/StatutLivraison";


@Injectable({
  providedIn: 'root'
})
export class LivraisonService {

  private baseUrl = 'http://localhost:8181';

  constructor(private http: HttpClient) { }
  getAllLivraisons(): Observable<Livraison[]> {
    return this.http.get<Livraison[]>(`${this.baseUrl}/admin/livraisons`);
  }
  assignerLivreur(id: number, livreurId: number): Observable<Livraison> {
    return this.http.post<Livraison>(`${this.baseUrl}/admin/${id}/assigner-livreur/${livreurId}`, {});
  }

  associerLivraison(annonceId: number, livraisonId: number): Observable<Annonce> {
    return this.http.post<Annonce>(`${this.baseUrl}/user/api/livraisons/${annonceId}/associer-livraison/${livraisonId}`, {});
  }

  confirmerLivraison(id: number, montant: number): Observable<Livraison> {
    return this.http.post<Livraison>(`${this.baseUrl}/livreur/confirmer/${id}`, { montant });
  }

  createLivraison(livraison: {
    dateLivraison: Date;
    adresseVendeur: string;
    adresseAcheteur:string;
    montant: number;
    id: number;
    statut: StatutLivraison;
    annonceId: number
  }): Observable<Livraison> {
    return this.http.post<Livraison>(`${this.baseUrl}/user/api/livraisons`, livraison);
  }
  getLivraisonDetails(id: number): Observable<Livraison> {
    return this.http.get<Livraison>(`${this.baseUrl}/livraisons/${id}`);
  }
  getUserLivraisons(): Observable<Livraison[]> {
    return this.http.get<Livraison[]>(`${this.baseUrl}/livraisons`);
  }
  getLivreurLivraisons(): Observable<Livraison[]> {
    return this.http.get<Livraison[]>(`${this.baseUrl}/livraison`);
  }
  updateUserLivraison(id: number, updatedLivraison: Livraison): Observable<Livraison> {
    return this.http.put<Livraison>(`${this.baseUrl}/livraisons/${id}`, updatedLivraison);
  }
  deleteUserLivraison(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/livraisons/${id}`);
  }
}
