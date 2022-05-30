import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Training } from '../models/training.model';

@Injectable({
  providedIn: 'root'
})
export class TrainingService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private httpClient: HttpClient) {}

  // createTraining(training: Training, file: File): Observable<object> {
  //   return this.httpClient.post(`${this.apiServerUrl}/trainings`, file);
  // }

  createTraining(training: Training, currentFile: File): Observable<Training> {
    const formData: FormData = new FormData();
    formData.append("training", new Blob([JSON.stringify(training)], { type: "application/json" }));
    formData.append('currentFile', currentFile);

    return this.httpClient.post<Training>(`${this.apiServerUrl}/trainings`, formData, {
        reportProgress: true,
        responseType: 'json',
    });
  }
}
