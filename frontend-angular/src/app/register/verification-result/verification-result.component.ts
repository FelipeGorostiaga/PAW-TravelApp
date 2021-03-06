import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../../services/auth/auth.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

declare var require: any;

@Component({
    selector: 'app-verification-result',
    templateUrl: './verification-result.component.html',
    styleUrls: ['./verification-result.component.scss']
})
export class VerificationResultComponent implements OnInit {

    verified: boolean = false;
    notFound: boolean;
    serverError: boolean;

    errorImg = require('!!file-loader!../../../assets/images/red_cross.png').default;

    constructor(private route: ActivatedRoute,
                private authService: AuthService,
                private spinner: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.spinner.show();
        this.verified = false;
        let verificationCode = this.route.snapshot.queryParams['code'];
        if (!verificationCode) {
            this.notFound = true;
            this.spinner.hide();
            return;
        }
        this.authService.verifyAccount(verificationCode).subscribe(
            data => {
                this.verified = true;
                this.authService.createSession(data.accessToken, data.refreshToken, data.user);
                this.spinner.hide();
            },
            error => {
                switch (error.status) {
                    case 404:
                        this.notFound = true;
                        break;
                    case 500:
                        this.serverError = true;
                        break;
                }
                this.spinner.hide();
            }
        );
    }

}
