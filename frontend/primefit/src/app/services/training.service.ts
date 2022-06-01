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

  getTrainings(): Observable<Training[]> {
    return this.httpClient.get<Training[]>(`${this.apiServerUrl}/trainings`);
  }

  createTraining(training: Training, currentFile: File): Observable<Training> {
    const formData: FormData = new FormData();
    formData.append("training", new Blob([JSON.stringify(training)], { type: "application/json" }));
    formData.append('currentFile', currentFile);

    return this.httpClient.post<Training>(`${this.apiServerUrl}/trainings`, formData, {
        reportProgress: true,
        responseType: 'json',
    });
  }

  delete(trainingId: number) {
    return this.httpClient.delete<Training>(`${this.apiServerUrl}/trainings/${trainingId}`);
  }

  send(trainingId: number, userId: number) {
    return this.httpClient.post<Training>(`${this.apiServerUrl}/trainings/sendTrainingPlan/${trainingId}/${userId}`, null);
  }

}
