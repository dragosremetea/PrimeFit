import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserLoginComponent } from './authentication/user-login/user-login.component';
import { UserRegisterComponent } from './authentication/user-register/user-register.component';
import { UserComponent } from './dashboard/components/user/user.component';

const routes: Routes = [
  { path: 'register', component: UserRegisterComponent},
  { path: 'login', component: UserLoginComponent},
  { path: '', component: UserComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
