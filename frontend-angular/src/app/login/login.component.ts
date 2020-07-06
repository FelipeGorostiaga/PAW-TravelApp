import { Component, OnInit } from '@angular/core';
import {ApiUserService} from '../services/api-user.service';
import {UserAuth} from '../model/user-auth';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;
  rememberMe: boolean;
  message: any;

  constructor(private userService: ApiUserService, private router: Router) { }

  ngOnInit() {
  }

  login() {
    const userDetails = new UserAuth(this.username, this.password);
    this.userService.authenticateUser(userDetails).subscribe(
        res => {
          console.log(res)
          // Todo: set jwt in auth service?
          this.router.navigate(["/home"]);
        },
        err => {
          alert("Error in login");
          this.message = "Invalid username or password";
        }
    );
  }
}
