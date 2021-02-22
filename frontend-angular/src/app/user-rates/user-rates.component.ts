import {Component, OnInit} from '@angular/core';
import {ApiUserService} from "../services/api-user.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {ActivatedRoute, Router} from "@angular/router";
import {Rate} from "../model/rate";
import {User} from "../model/user";
import {forkJoin} from "rxjs";

@Component({
    selector: 'app-user-rates',
    templateUrl: './user-rates.component.html',
    styleUrls: ['./user-rates.component.scss']
})
export class UserRatesComponent implements OnInit {

    currentPage: number;
    numberOfPages: number;
    ratesPerPage = 3;
    rates: Rate[];

    user: User;
    userRating: number;

    error: boolean;

    constructor(private userService: ApiUserService,
                private spinner: NgxSpinnerService,
                private route: ActivatedRoute,
                private router: Router) {
    }

    ngOnInit(): void {
        this.currentPage = 0;
        this.spinner.show();
        const userId = Number(this.route.snapshot.paramMap.get("id"));
        if (!userId) {
            this.router.navigate(['/404']);
        }
        let user$ = this.userService.getUserById(userId);
        let rating$ = this.userService.getUserRating(userId);
        let rates$ = this.userService.getUserRates(userId);
        forkJoin([user$ , rating$, rates$]).subscribe(
            data => {
                this.user = data[0];
                this.userRating = data[1];
                this.rates = data[2];
                this.spinner.hide();
            },
            error => {
                this.spinner.hide();
                this.error = true;
            }
        );

    }

    updatePage(newPage: number) {
        this.currentPage = newPage;
    }
}
