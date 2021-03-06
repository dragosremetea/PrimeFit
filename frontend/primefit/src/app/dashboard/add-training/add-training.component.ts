import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Training, TrainingIntensity } from 'src/app/models/training.model';
import { AuthService } from 'src/app/services/auth.service';
import { TrainingService } from 'src/app/services/training.service';
import { SidenavComponent } from '../sidenav/sidenav.component';

@Component({
  selector: 'app-add-training',
  templateUrl: './add-training.component.html',
  styleUrls: ['./add-training.component.css']
})
export class AddTrainingComponent implements OnInit {

  intensity: TrainingIntensity[] = [
    TrainingIntensity.LOW,
    TrainingIntensity.MEDIUM,
    TrainingIntensity.EXTREME
  ];

  selectedFile: any = null;

  training: Training = new Training();

  id: any;

  dataSource: Training[] = [];

  displayedColumns: string[] = ['Name', 'Duration', 'Intensity', 'Actions'];

  TrainingIntensity = TrainingIntensity;

  constructor(
    public authService: AuthService,
    public trainingService: TrainingService,
    public router: Router,
    public sidenav: SidenavComponent
  ) { }

  ngOnInit(): void {
    this.getAllTrainings();
  }

  onFileSelected(event: any): void {
      this.selectedFile = event.target.files[0] ?? null;
  }

  saveTraining() {
    this.trainingService.createTraining(this.training, this.selectedFile).subscribe(data => {
      this.router.navigate(['/dashboard']);
      this.refresh();
      this.training.name = "";
      this.training.duration = 0;
      this.training.pdfUrl = "";
      this.selectedFile = "";
    })
  }

  deleteTraining(row: any) {
    this.trainingService.delete(row.id).subscribe(data => {
      this.refresh();
    })
  }

  sendEmail(row: any) {
    this.id = localStorage.getItem('currentUser')
    this.trainingService.send(row.id, parseInt(this.id)).subscribe(data => {
    });
  }

  getAllTrainings() {
    this.trainingService.getTrainings().subscribe(data => {
      this.dataSource = data;
    })
  }

  refresh(): void {
    this.getAllTrainings();
  }

  getTrainingIntensityAsString(intensities: TrainingIntensity[]): string {
    let intensityAsString = "";
    intensities.filter((x: TrainingIntensity) => {
      if(x == 0) {
        intensityAsString = 'LOW';
      }
      if(x == 1) {
        intensityAsString = 'MEDIUM';
      }
      if(x == 2) {
        intensityAsString = 'EXTREME';
      }
    });
    return intensityAsString;
  }
}
