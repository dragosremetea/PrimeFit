import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Role } from 'src/app/models/role.model';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css']
})
export class UserRegisterComponent implements OnInit {

  role: Role = new Role(2, "CLIENT");
  roles: Role[] = [];
  user: User = new User();

  constructor(
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  userRegister() {
    this.roles.push(this.role);
    this.user.roles = this.roles;
    this.userService.register(this.user).subscribe(data => {
      this.router.navigate(['/']);
    }, error => alert("Failed"))
    this.router.navigate(['/register']);
  }

}
