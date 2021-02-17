import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../../services/auth/auth.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

@Component({
    selector: 'app-verification-result',
    templateUrl: './verification-result.component.html',
    styleUrls: ['./verification-result.component.scss']
})
export class VerificationResultComponent implements OnInit {

    constructor(private route: ActivatedRoute,
                private authService: AuthService,
                private spinner: NgxSpinnerService) {
    }

    verified: boolean = false;

    notFound: boolean;
    serverError: boolean;

    ngOnInit(): void {
        this.spinner.show();
        this.verified = false;
        let verificationCode = this.route.snapshot.queryParams['code'];
        if (!verificationCode) {
            this.notFound = true;
            return;
        }
        this.authService.verifyAccount(verificationCode).subscribe(
            data => {
                this.verified = true;
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
