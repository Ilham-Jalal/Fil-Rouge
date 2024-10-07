import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError } from 'rxjs';
import { DecodejwtService } from './decodejwt-service.service';
import { LoginRequest } from '../dto/LoginRequest';
import { SignUpRequest } from '../dto/SignUpRequest';
import { Role } from "../enum/Role";
import { User } from "../model/User";
import { UserDTO } from "../dto/UserDTO";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8180';

  constructor(
    private http: HttpClient,
    private decodejwtService: DecodejwtService
  ) {}

  login(loginRequest: LoginRequest): Observable<{ token: string; role: string }> {
    return this.http.post<{ token: string; role: string }>(`${this.apiUrl}/login`, loginRequest);
  }

  logout() {
    localStorage.removeItem('jwt');
  }

  getCurrentUserRole(): any | null {
    const token = localStorage.getItem('jwt');
    if (token) {
      const decodedToken = this.decodejwtService.decodeToken(token);
      return decodedToken?.roles || null;
    }
    return null;
  }

  findUtilisateurByUsername(username: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/user/${username}`)
  }

  findIdByUsername(username: string | null): Observable<any> {
    return this.http.get(`${this.apiUrl}/findi?username=${username}`);
  }

  findAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/admin/users`);
  }

  getCurrentUserId(): string | null {
    const token = localStorage.getItem('jwt');
    if (token) {
      const decodedToken = this.decodejwtService.decodeToken(token);
      return decodedToken?.sub || null;  // Assuming 'sub' contains the user ID
    }
    return null;
  }
}
