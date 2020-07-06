import { Component, OnInit } from '@angular/core';
import {ApiUserService} from '../services/api-user.service';
import {UserAuth} from '../model/user-auth';

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

  constructor(private userService: ApiUserService) { }

  ngOnInit() {
  }

  login() {
    const userDetails = new UserAuth(this.username, this.password);
    this.userService.authenticateUser(userDetails).subscribe(data => {
      console.log(data);
    });
  }
}
