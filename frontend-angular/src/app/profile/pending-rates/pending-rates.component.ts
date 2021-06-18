import {Component, Input, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {ActivatedRoute, Router} from "@angular/router";
import {ApiUserService} from "../../services/api-user.service";
import {User} from "../../model/user";

@Component({
    selector: 'app-pending-rates',
    templateUrl: './pending-rates.component.html',
    styleUrls: ['./pending-rates.component.scss']
})
export class PendingRatesComponent implements OnInit {

    @Input() user: User;

    maxShowedPendingRates = 3;
    loading: boolean;
    error: boolean;
    sentReview: boolean;

    constructor(private spinner: NgxSpinnerService,
                private route: ActivatedRoute,
                private router: Router,
                private userService: ApiUserService) {
    }

    ngOnInit() {
        this.userService.getUserPendingRates(this.user.pendingRatesURL).subscribe(
            res => {
                this.user.pendingRates = res;
            },
            () => {
                this.error = true;
            }
        );
    }

    submitRate(data: any) {
        if (this.loading) {
            return;
        }
        this.sentReview = false;
        this.loading = true;
        let rate = data.rate;
        this.userService.rateUser({
            rateId: data.id,
            comment: data.comment,
            rate: data.rating
        }).subscribe(
            () => {
                let index = this.user.pendingRates.indexOf(rate);
                this.user.pendingRates.splice(index, 1);
                this.loading = false;
                this.sentReview = true;
            }, () => {
                this.loading = false;
            }
        );
    }
}
