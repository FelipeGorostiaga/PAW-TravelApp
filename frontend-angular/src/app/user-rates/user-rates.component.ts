import {Component, OnInit} from '@angular/core';
import {ApiUserService} from "../services/api-user.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {ActivatedRoute} from "@angular/router";
import {Rate} from "../model/rate";
import {User} from '../model/user';

@Component({
    selector: 'app-user-rates',
    templateUrl: './user-rates.component.html',
    styleUrls: ['./user-rates.component.scss']
})
export class UserRatesComponent implements OnInit {


    rates: Rate[];
    rating: number;
    user: User;
    error: boolean;

    constructor(private userService: ApiUserService,
                private spinner: NgxSpinnerService,
                private route: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.spinner.show();
        this.route.params.subscribe(params => {
            const userId = params['id'];
            this.userService.getUser(userId).subscribe(
                data => {
                    this.user = data;
                    this.userService.getUserRates(this.user.ratesURL).subscribe(
                        data => {
                            this.rates = data;
                            this.calculateUserRate();
                            this.spinner.hide();
                        }
                    );
                },
                () => {
                    this.error = true;
                }
            );
        });
    }

    private calculateUserRate() {
        let totalRate = 0;
        const len = this.rates.length;
        if (len === 0) {
            return;
        }
        this.rates.forEach(rate => totalRate += rate.rate);
        this.rating = totalRate / len;
    }

}
