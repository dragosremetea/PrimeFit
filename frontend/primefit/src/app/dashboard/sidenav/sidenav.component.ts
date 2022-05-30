import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MaterialModule } from 'src/app/material.module';
import { Training, TrainingIntensity } from 'src/app/models/training.model';
import { AuthService } from 'src/app/services/auth.service';
import { TrainingService } from 'src/app/services/training.service';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent implements OnInit {

  intensity: TrainingIntensity[] = [
    // { id: "Low", name: "Low", TrainingIntensity: 'LOW' },
    // { id: "Medium", name: "Medium", TrainingIntensity: 'MEDIUM' },
    // { id: "Extreme", name: "Extreme", TrainingIntensity: 'EXTREME' }
    TrainingIntensity.LOW,
    TrainingIntensity.MEDIUM,
    TrainingIntensity.EXTREME
  ];

  selectedFile: any = null;

  training: Training = new Training();

  constructor(
    public authService: AuthService,
    public trainingService: TrainingService
  ) { }

  ngOnInit(): void {
  }

  onFileSelected(event: any): void {
      this.selectedFile = event.target.files[0] ?? null;
  }

  saveTraining() {
    console.log(this.training.name);
    console.log(this.training.trainingIntensity);
    console.log(this.training.duration);
    console.log(this.selectedFile);
    console.log(this.training);
    this.trainingService.createTraining(this.training, this.selectedFile).subscribe(data => {
    })
  }

}
