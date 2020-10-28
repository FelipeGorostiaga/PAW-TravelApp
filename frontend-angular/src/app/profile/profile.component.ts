import {Component, OnInit} from '@angular/core';
import {User} from "../model/user";
import {ApiUserService} from "../services/api-user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../services/auth/auth.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  loggedUser: User;
  user: User;
  profilePicture: any;

  constructor(private us: ApiUserService, private authService: AuthService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
      this.loggedUser = this.authService.getLoggedUser();
      const profileId = Number(this.route.snapshot.paramMap.get("id"));
      this.us.getUserById(profileId).subscribe(
            data => {
                console.log(data);
                this.user = data;
            },
            error => {
                console.log(error);
                this.router.navigate(['/404']);
            });
  }

}
