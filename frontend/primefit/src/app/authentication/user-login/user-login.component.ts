import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Role } from 'src/app/models/role.model';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  user: User = new User();
  valid: Boolean = false;

  constructor(
    private userService: UserService,
    private router: Router
    ) { }

  ngOnInit(): void {
  }

  userLogin() {
    this.userService.loginUser(this.user).subscribe(data => {
      this.router.navigate(['/dashboard']);
    }, error => this.router.navigate(['/register']))
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }

}
