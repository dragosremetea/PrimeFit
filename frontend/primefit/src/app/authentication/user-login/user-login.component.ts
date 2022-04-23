import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  user: User = new User();

  constructor(private loginservice: LoginService) { }

  ngOnInit(): void {
  }

  userLogin() {
    console.log(this.user);
    this.loginservice.loginUser(this.user).subscribe(data => {
      alert("Login Successfully")
    }, error => alert("Login Failed"))
  }

}
