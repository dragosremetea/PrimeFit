import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserService } from './services/user.service';
import { UserLoginComponent } from './authentication/user-login/user-login.component';
import { UserComponent } from './dashboard/user/user.component';
import { UserRegisterComponent } from './authentication/user-register/user-register.component';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { SidenavComponent } from './dashboard/sidenav/sidenav.component';
import { AddTrainingComponent } from './dashboard/add-training/add-training.component';
import { AddDietComponent } from './dashboard/add-diet/add-diet.component';
import { RemindersComponent } from './dashboard/reminders/reminders.component';

@NgModule({
  declarations: [
    AppComponent,
    UserComponent,
    UserLoginComponent,
    UserRegisterComponent,
    SidenavComponent,
    AddTrainingComponent,
    AddDietComponent,
    RemindersComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
  ],
  exports: [
    RouterModule
  ],
  providers: [UserService],
  bootstrap: [AppComponent]
})
export class AppModule { }
