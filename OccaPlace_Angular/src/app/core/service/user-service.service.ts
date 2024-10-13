import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'; // Import HttpClient
import {catchError, Observable, throwError} from 'rxjs';
import { SignUpRequest } from '../dto/SignUpRequest';
import { User } from '../model/User';
import { Role } from '../enum/Role';
import { UserDTO } from '../dto/UserDTO';
import {Livreur} from "../model/Livreur";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8181';

  constructor(private http: HttpClient) { }

  signUpUser(signUpRequest: SignUpRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/signup`, signUpRequest);
  }

  addUserByAdmin(role: Role, userDTO: UserDTO): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/admin/add/${role}`, userDTO);
  }

  getAllUsers(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.apiUrl}/admin/users`);
  }
  getAllLivreurs(): Observable<Livreur[]> {
    return this.http.get<Livreur[]>(`${this.apiUrl}/admin/livreurs`);
  }
  logout(): void {
    localStorage.removeItem('jwt');
  }

  countTotalUsers(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/admin/countUsers`).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération du nombre total d\'annonces:', error);
        return throwError(error);
      })
    );
  }

  countTotalLivreurs(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/admin/countLivreurs`).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération du nombre total d\'annonces:', error);
        return throwError(error);
      })
    );
  }
}
