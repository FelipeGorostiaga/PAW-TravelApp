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
  message: any;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
  }

  login(event) {
    event.preventDefault();
    this.authService.login(this.user).subscribe(
        res => {
          console.log(res);
          /*this.authService.setJwtToken(res);*/
          this.router.navigate(["/home"]);
        },
        err => {
          alert("Error in login");
          this.message = "Invalid username or password";
        }
    );
  }
}
