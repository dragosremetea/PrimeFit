import { Injectable } from '@angular/core';
import { BehaviorSubject, tap } from 'rxjs';
import { User } from '../models/user.model';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _isLoggedIn$ = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this._isLoggedIn$.asObservable();
  user!: User;
  userObject!: User;
  persistedUser!: User;

  get loggedUser(): any {
    return localStorage.getItem('currentUser');
  }

  constructor(
    private userService: UserService
  ) { 
    this._isLoggedIn$.next(!!this.loggedUser);
    // this.user = JSON.parse(this.loggedUser);
  }

  login(user: User) {
    return this.userService.loginUser(user).pipe(
      tap((data: any) => {
        localStorage.setItem('currentUser', data);
        this._isLoggedIn$.next(true);
        this.user = data;
      })
    );
  }

  private getUser(username: string): User {

    this.userService.getUserByUsername(username).subscribe(data => {
      this.persistedUser = data;
    });

    return this.persistedUser;
  }
}
