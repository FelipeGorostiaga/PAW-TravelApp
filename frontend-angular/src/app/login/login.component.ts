import {Component, OnInit} from '@angular/core';
import {UserAuth} from '../model/user-auth';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../services/auth/auth.service';
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";

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

    credentialsError: boolean;
    verificationError: boolean;
    serverError: boolean;

    returnUrl: string;

    constructor(private authService: AuthService,
                private router: Router,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.authService.logout();
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
    }

    login() {
        if (this.user.username.length === 0 || this.user.password.length === 0) {
            return;
        }
        this.authService.login(this.user).subscribe(
            data => {
                this.authService.createSession(data.accessToken, data.refreshToken, data.user);
                this.router.navigateByUrl(this.returnUrl);
            },
            error => {
                if (error.status === 403) {
                    let field = error.error.invalidField;
                    if (field === 'verification') {
                        this.verificationError = true;
                    } else if (field === 'credentials') {
                        this.credentialsError = true;
                    }
                } else {
                    this.serverError = true;
                }
            }
        );
    }

    closeCredentialsAlert() {
        this.credentialsError = false;
    }

    closeVerificationAlert() {
        this.verificationError = false;
    }

    closeServerErrAlert() {
        this.serverError = false;
    }
}
