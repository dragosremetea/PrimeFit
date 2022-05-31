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

  trainingButton: boolean = true;
  dietsButton: boolean = false;
  remindersButton: boolean = false;

  constructor(
    public authService: AuthService, 
    public router: Router
    ) { }

  ngOnInit(): void {
  }

  pressTrainings() {
    this.trainingButton = true;
    this.dietsButton = false;
    this.remindersButton = false;
    console.log(this.trainingButton);
  }

  pressDiets() {
    this.trainingButton = false;
    this.dietsButton = true;
    this.remindersButton = false;
  }

  pressReminders() {
    this.trainingButton = false;
    this.dietsButton = false;
    this.remindersButton = true;
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }
}
