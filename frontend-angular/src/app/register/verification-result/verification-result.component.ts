import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../../services/auth/auth.service";

@Component({
    selector: 'app-verification-result',
    templateUrl: './verification-result.component.html',
    styleUrls: ['./verification-result.component.scss']
})
export class VerificationResultComponent implements OnInit {

    constructor(private route: ActivatedRoute, private authService: AuthService) {
    }

    successfullyVerified: boolean = false;
    errorMessage: string;

    ngOnInit(): void {
        let verificationCode = this.route.snapshot.paramMap.get('code');
        this.authService.verifyAccount(verificationCode).subscribe(
            data => {
                this.successfullyVerified = true;
            },
            error => {
                console.log(error);
                this.errorMessage = error.message;
            }
        );
    }

}
