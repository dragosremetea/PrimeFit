import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private baseUrl = "http://localhost:8080/users/login";
  constructor(private httpClient: HttpClient) { }

    loginUser(user: User): Observable<object> {
      console.log("in service: " + user.username);
      return this.httpClient.post(`${this.baseUrl}`, user);
    }
}
