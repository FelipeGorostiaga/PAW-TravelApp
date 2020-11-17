import {Component, OnInit} from '@angular/core';
import {UserAuth} from '../model/user-auth';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  user: UserAuth = {
    username: "",
    password: ""
  };

  rememberMe: boolean;
  errMessage: string;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
  }

  login() {
    this.authService.login(this.user).subscribe(
        data => {
            this.authService.createSession(data.accessToken, data.refreshToken, data.user);
            this.router.navigate(["/home"]);
        },
        err => {
          this.errMessage = "Invalid username or password";
        }
    );
  }
}
