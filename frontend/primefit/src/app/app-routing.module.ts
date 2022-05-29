import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserLoginComponent } from './authentication/user-login/user-login.component';
import { UserRegisterComponent } from './authentication/user-register/user-register.component';
import { UserComponent } from './dashboard/user/user.component';

const routes: Routes = [
  { path: 'register', component: UserRegisterComponent},
  { path: '', component: UserLoginComponent},
  { path: 'dashboard', component: UserComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
