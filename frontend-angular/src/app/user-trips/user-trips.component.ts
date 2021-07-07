import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RoutesRecognized} from "@angular/router";
import {Trip} from "../model/trip";
import {ApiUserService} from "../services/api-user.service";
import {AuthService} from "../services/auth/auth.service";
import {User} from "../model/user";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

@Component({
    selector: 'app-user-trips',
    templateUrl: './user-trips.component.html',
    styleUrls: ['./user-trips.component.scss']
})
export class UserTripsComponent implements OnInit {

    trips: Trip[];
    loggedUser: User;
    loading = true;
    numberOfPages: number;
    currentPage: number;
    totalTrips: number;
    serverError: boolean;

    constructor(private userService: ApiUserService, private authService: AuthService,
                private router: Router,
                private spinner: NgxSpinnerService,
                private route: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.loggedUser = this.authService.getLoggedUser();
        this.route.queryParams.subscribe(params => {
            this.currentPage = params['page'] || 1;
            if (Number(this.currentPage)) {
                this.getPageTrips(this.currentPage);
            } else {
                this.navigateNotFound();
            }
        });

        this.router.events.subscribe((val) => {
                if (val instanceof RoutesRecognized) {
                    const url = val.state.url;
                    if (url.includes('?page=', 4)) {
                        let maybePage = url.slice(17, url.length);
                        if (Number(maybePage)) {
                            this.getPageTrips(Number(maybePage));
                        } else {
                            this.navigateNotFound();
                        }
                    }
                }
            }
        );
    }

    getPageTrips(page: number) {
        this.currentPage = page;
        this.spinner.show();
        this.userService.getUserTrips(this.loggedUser.id, page).subscribe(
            data => {
                this.trips = data.trips;
                this.numberOfPages = data.maxPage;
                this.totalTrips = data.totalAmount;
                this.spinner.hide();
            },
            err => {
                switch (err.status) {
                    case (400):
                        this.spinner.hide();
                        this.navigateNotFound();
                        break;
                    case (404):
                        this.spinner.hide();
                        this.navigateNotFound();
                        break;
                    case (500):
                        this.spinner.hide();
                        this.serverError = true;
                        break;
                }
                this.spinner.hide();
            }
        );
    }

    updatePage(newPage) {
        if (this.currentPage === newPage) {
            return;
        }
        this.getPageTrips(newPage);
    }

    navigateNotFound() {
        this.router.navigate(["/404"]);
    }
}
