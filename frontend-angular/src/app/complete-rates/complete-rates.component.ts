import {Component, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {ActivatedRoute, Router} from "@angular/router";
import {ApiUserService} from "../services/api-user.service";
import {Rate} from "../model/rate";


@Component({
    selector: 'app-complete-rates',
    templateUrl: './complete-rates.component.html',
    styleUrls: ['./complete-rates.component.scss']
})
export class CompleteRatesComponent implements OnInit {

    pendingRates: Rate[];

    constructor(private spinner: NgxSpinnerService,
                private route: ActivatedRoute,
                private router: Router,
                private userService: ApiUserService) {
    }

    ngOnInit() {
        this.spinner.show();
        const userId = Number(this.route.snapshot.paramMap.get("id"));
        this.userService.getUserPendingRates(userId).subscribe(
            res => {
                console.log(res);
                this.pendingRates = res;
                this.spinner.hide();
            },
            error => {
                this.spinner.hide();
                console.log(error);
            }
        );
    }

    submitRate(data: any) {
        let rate = data.rate;
        this.userService.submitRate({
            rateId: data.id,
            comment: data.comment,
            rate: data.rating
        }).subscribe(
            ok => {
                let index = this.pendingRates.indexOf(rate);
                this.pendingRates.splice(index, 1);
            },
            error => {
                console.log(error);
            }
        );
    }
}
