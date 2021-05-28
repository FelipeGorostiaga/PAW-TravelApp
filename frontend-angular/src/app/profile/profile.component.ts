import {Component, OnInit} from '@angular/core';
import {ApiUserService} from "../services/api-user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../services/auth/auth.service";
import {User} from "../model/user";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

    user: User;
    isProfileOwner: boolean;
    loading: boolean;

    selectedIndex: number;

    constructor(private userService: ApiUserService,
                private authService: AuthService,
                private router: Router,
                private route: ActivatedRoute,
                private spinner: NgxSpinnerService) {
    }

    ngOnInit() {
        this.selectedIndex = 0;
        this.spinner.show();
        this.loading = true;
        const profileId = Number(this.route.snapshot.paramMap.get("id"));
        if (!profileId) {
            this.spinner.hide();
            this.navigateNotFound();
        }
        this.userService.getUser(profileId).subscribe(
            data => {
                console.log(data);
                this.user = data;
                this.isProfileOwner = this.authService.getLoggedUser().id === this.user.id;
                this.loading = false;
                this.spinner.hide();
            },
            () => {
                this.spinner.hide();
                this.navigateNotFound();
            }
        );
    }

    switchTab(index: number) {
        this.selectedIndex = index;
    }

    navigateNotFound() {
        this.router.navigate(['/404']);
    }

}
