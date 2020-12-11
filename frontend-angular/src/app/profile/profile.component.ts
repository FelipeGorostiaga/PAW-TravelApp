import {Component, OnInit} from '@angular/core';
import {User} from "../model/user";
import {ApiUserService} from "../services/api-user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../services/auth/auth.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

    loggedUser: User;

    // User of current profile url
    user: User;

    loading: boolean;

    // TODO
    profilePicture: any;

    constructor(private us: ApiUserService,
                private authService: AuthService,
                private router: Router,
                private route: ActivatedRoute,
                private spinner: NgxSpinnerService) {
    }

    ngOnInit() {
        this.loading = true;
        this.spinner.show();
        this.loggedUser = this.authService.getLoggedUser();
        const profileId = Number(this.route.snapshot.paramMap.get("id"));
        this.us.getUserById(profileId).subscribe(
            data => {
                this.user = data;
                this.loading = false;
                this.spinner.hide();
            },
            error => {
                console.log(error);
                this.spinner.hide();
                this.loading = false;
                this.router.navigate(['/404']);
            });
    }

}
