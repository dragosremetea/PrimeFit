import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { User } from "../models/user.model";
import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private apiServerUrl = environment.apiBaseUrl;

    private baseUrl = "http://localhost:8080/users/login";
    constructor(private httpClient: HttpClient) {}

    loginUser(user: User): Observable<object> {
        return this.httpClient.post(`${this.apiServerUrl}/users/login`, user);
    }

    register(user: User): Observable<object> {
        return this.httpClient.put(`${this.apiServerUrl}/users`, user);
    }

    getUsers(): Observable<User[]> {
        return this.httpClient.get<User[]>(`${this.apiServerUrl}/users`);
    }


}