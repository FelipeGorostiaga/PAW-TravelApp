import { Component, OnInit } from '@angular/core';
import {AuthService} from "../services/auth/auth.service";
import {ApiUserService} from "../services/api-user.service";
import {Router} from "@angular/router";
import {User} from "../model/user";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {

  isLoggedIn: boolean;
  loggedUser: User;
  userImage: any;

  constructor(private authService: AuthService, private apiService: ApiUserService, private router: Router) { }

  ngOnInit() {
    this.isLoggedIn = this.authService.isLoggedIn();
    if (this.isLoggedIn) {
      this.loggedUser = this.authService.getLoggedUser();
    }
  }

  search() {
    // TODO
  }

  logout() {
    this.authService.logout();
  }

}
