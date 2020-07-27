import { Component, OnInit } from '@angular/core';
import {User} from "../model/user";
import {ApiUserService} from "../services/api-user.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  loggedUser: User;
  user: User;
  hasProfilePicture: boolean;
  profilePicture: any;



  constructor(private us: ApiUserService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    const profileId = Number(this.route.snapshot.paramMap.get("id"));
    this.us.getUserById(profileId).subscribe(
        data => {
          console.log("user data:");
          this.user = data;
          console.log(data);
        },
        error => {
          // todo: not found error
          console.log(error);
        }
    );
  }

}
