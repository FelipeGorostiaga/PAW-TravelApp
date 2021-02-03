import {Component, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {ActivatedRoute, Router} from "@angular/router";
import {ApiUserService} from "../services/api-user.service";
import {Rate} from "../model/rate";
import {FormBuilder} from "@angular/forms";


@Component({
    selector: 'app-complete-rates',
    templateUrl: './complete-rates.component.html',
    styleUrls: ['./complete-rates.component.scss']
})
export class CompleteRatesComponent implements OnInit {


    pendingRates: Rate[];
    rating = 0;
    comment: string = "";

    constructor(private spinner: NgxSpinnerService,
                private route: ActivatedRoute,
                private router: Router,
                private userService: ApiUserService,
                private formBuilder: FormBuilder) {
    }

    ngOnInit() {
        this.spinner.show();
        const userId = Number(this.route.snapshot.paramMap.get("id"));
        this.userService.getUserPendingRates(userId).subscribe(
            res => {
                this.pendingRates = res;
                this.spinner.hide();
            },
            error => {
                console.log(error);
            }
        );
    }

    submitRate() {
        console.log("submitting rate");
    }
}
