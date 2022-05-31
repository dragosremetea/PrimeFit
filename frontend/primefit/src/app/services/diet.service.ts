import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Diet } from '../models/diet.model';

@Injectable({
  providedIn: 'root'
})
export class DietService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private httpClient: HttpClient) {}

  getDiets(): Observable<Diet[]> {
    return this.httpClient.get<Diet[]>(`${this.apiServerUrl}/diets`);
  }

  createDiet(diet: Diet, currentFile: File): Observable<Diet> {
    const formData: FormData = new FormData();
    formData.append("diet", new Blob([JSON.stringify(diet)], { type: "application/json" }));
    formData.append('currentFile', currentFile);

    return this.httpClient.post<Diet>(`${this.apiServerUrl}/diets`, formData, {
        reportProgress: true,
        responseType: 'json',
    });
  }

  delete(dietId: number) {
    return this.httpClient.delete<Diet>(`${this.apiServerUrl}/diets/${dietId}`);
  }
}
