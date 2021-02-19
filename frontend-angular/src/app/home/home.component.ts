import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {ApiTripService} from "../services/api-trip.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {Trip} from "../model/trip";
import {ActivatedRoute, NavigationStart, Router, RoutesRecognized} from "@angular/router";
import * as url from "url";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

    trips: Trip[];
    currentPage: number;
    numberOfPages: number;
    totalTrips: number;

    serverError: boolean;

    constructor(private ts: ApiTripService,
                private spinner: NgxSpinnerService,
                private route: ActivatedRoute,
                private router: Router) {
    }

    ngOnInit() {
        this.currentPage = this.route.snapshot.queryParams['page'] || 1;
        if (Number(this.currentPage)) {
            this.getPageTrips(this.currentPage);
        } else {
            this.navigateNotFound();
        }
        this.router.events.subscribe((val) => {
                if (val instanceof RoutesRecognized) {
                    const url = val.state.url;
                    if (url.includes('?page=', 4)) {
                        let maybePage = url.slice(11, url.length);
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
        this.spinner.show();
        this.ts.getTripsForPage(page).subscribe(
            data => {
                this.trips = data.trips;
                this.numberOfPages = data.maxPage;
                this.totalTrips = data.totalAmount;
                this.spinner.hide();
                this.currentPage = page;
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
        this.currentPage = newPage;
        this.getPageTrips(newPage);
    }

    navigateNotFound() {
        this.router.navigate(["/404"]);
    }

}
