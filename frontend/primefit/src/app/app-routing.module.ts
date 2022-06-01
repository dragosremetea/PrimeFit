import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserLoginComponent } from './authentication/user-login/user-login.component';
import { UserRegisterComponent } from './authentication/user-register/user-register.component';
import { AddTrainingComponent } from './dashboard/add-training/add-training.component';
import { UserComponent } from './dashboard/user/user.component';
import { HasRoleGuard } from './has-role.guard';
import { IsAuthenticatedGuard } from './is-authenticated.guard';

const routes: Routes = [
  {
    path: 'login',
    component: UserLoginComponent
  },
  {
    path: 'dashboard',
    component: UserComponent,
    canActivate: [IsAuthenticatedGuard],
  },
  {
    path: 'addTraining',
    component: AddTrainingComponent,
    canActivate: [IsAuthenticatedGuard],
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login'
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
