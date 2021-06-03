import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {ApiUserService} from "../../services/api-user.service";

@Component({
    selector: 'app-rates',
    templateUrl: './rates.component.html',
    styleUrls: ['./rates.component.scss']
})
export class RatesComponent implements OnInit {

    @Input() user: User;
    @Input() isProfileOwner: boolean;

    currentPage: number;
    numberOfPages: number;
    maxShowedRates = 3;

    hasRates: boolean;

    userRating: number;

    error: boolean;

    constructor(private userService: ApiUserService) {
    }

    ngOnInit(): void {
        this.currentPage = 0;
        if (!this.user.rates) {
            this.userService.getUserRates(this.user.ratesURL).subscribe(
                data => {
                    this.user.rates = data;
                    this.calculateUserRate();
                },
                () => {
                    this.error = true;
                }
            );
        } else {
            if (!this.user.rating) {
                this.calculateUserRate();
            }
        }
    }

    private calculateUserRate() {
        let totalRate = 0;
        const len = this.user.rates.length;
        if (len == 0) {
            this.hasRates = false;
            return;
        }
        this.hasRates = true;
        this.user.rates.forEach(rate => totalRate += rate.rate);
        this.user.rating = totalRate / len;
    }

}
