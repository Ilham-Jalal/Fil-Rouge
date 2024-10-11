import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DecodejwtService } from './decodejwt-service.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8181';

  constructor(
    private http: HttpClient,
    private decodejwtService: DecodejwtService
  ) {}

  login(loginRequest: any): Observable<{ token: string; role: string }> {
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
    return this.http.get(`${this.apiUrl}/user/${username}`);
  }

  findIdByUsername(username: string | null): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/findi?username=${username}`);
  }

  getCurrentUsername(): string | null {
    const token = localStorage.getItem('jwt');
    if (token) {
      const decodedToken = this.decodejwtService.decodeToken(token);
      return decodedToken?.sub || null;
    }
    return null;
  }

  getCurrentUserId(): Observable<number> {
    const username = this.getCurrentUsername();
    if (username) {
      return this.findIdByUsername(username);
    }
    throw new Error('No current user found');
  }
}
