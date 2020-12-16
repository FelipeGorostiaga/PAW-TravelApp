import {Component, OnInit} from '@angular/core';
import {UserAuth} from '../model/user-auth';
import {ActivatedRoute, Router} from '@angular/router';
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
    returnUrl: string;

    constructor(private authService: AuthService,
                private router: Router,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.authService.logout();
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
        console.log(this.route.snapshot.queryParams['returnUrl']);
    }

    login() {
        this.authService.login(this.user).subscribe(
            data => {
                this.authService.createSession(data.accessToken, data.refreshToken, data.user);
                console.log("return URL: " + this.returnUrl);
                this.router.navigateByUrl(this.returnUrl);
            },
            error => {
                this.errMessage = error;
            }
        );
    }

}
