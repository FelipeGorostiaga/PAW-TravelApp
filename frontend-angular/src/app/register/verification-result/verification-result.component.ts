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
    error: boolean;

    ngOnInit(): void {
        this.spinner.show();
        this.error = false;
        let verificationCode = this.route.snapshot.paramMap.get('code');
        this.authService.verifyAccount(verificationCode).subscribe(
            data => {
                this.verified = true;
                this.spinner.hide();
            },
            error => {
                this.spinner.hide();
                this.error = true;
            }
        );
    }

}
