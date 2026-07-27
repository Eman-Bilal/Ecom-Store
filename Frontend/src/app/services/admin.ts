import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environments';

export interface RegisterAdminRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phone: string;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  private baseUrl = `${environment.apiUrl}/admins`;

  constructor(private http: HttpClient) {}

  register(admin: RegisterAdminRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, admin);
  }
}