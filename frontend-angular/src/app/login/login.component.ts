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
  message: string;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
  }

  login() {
    console.log("performing login");
    console.log(this.user);
    this.authService.login(this.user).subscribe(
        res => {
          console.log(res);
          this.authService.setJwtToken(res.jwt);
          this.authService.getUserFromServer().subscribe(
              data => {
                  console.log(data);
                  this.authService.setLoggedUser(data);
                  this.router.navigate(["/home"]);
              },
              error => {
                this.message = "Error from server, try again...";
              }
          );
        },
        err => {
          alert("Error in login");
          this.message = "Invalid username or password";
        }
    );
  }
}
