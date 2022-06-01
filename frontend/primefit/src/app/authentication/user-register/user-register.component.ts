import { Component, OnInit } from '@angular/core';
import { DateAdapter } from '@angular/material/core';
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
    private router: Router,
    private dateAdapter: DateAdapter<Date>
  ) { 
    this.dateAdapter.setLocale('en-GB'); //dd/MM/yyyy
  }

  ngOnInit(): void {
  }

  userRegister() {
    this.roles.push(this.role);
    this.user.roles = this.roles;
    this.userService.register(this.user).subscribe(data => {
      this.user.username = "";
      this.user.password = "";
      this.user.firstName = "";
      this.user.lastName = "";
      this.user.email = ""; 
      this.user.phoneNumber = "";
    }, error => alert("Failed"))
  }

}
