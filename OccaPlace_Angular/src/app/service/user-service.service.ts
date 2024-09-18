import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'; // Import HttpClient
import { Observable } from 'rxjs';
import { SignUpRequest } from '../dto/SignUpRequest';
import { User } from '../model/User';
import { Role } from '../enum/Role';
import { UserDTO } from '../dto/UserDTO';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8180';

  constructor(private http: HttpClient) { }

  signUpUser(signUpRequest: SignUpRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/signup`, signUpRequest);
  }

  addUserByAdmin(role: Role, userDTO: UserDTO): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/admin/add/${role}`, userDTO);
  }

  logout(): void {
    localStorage.removeItem('jwt');
  }
}
