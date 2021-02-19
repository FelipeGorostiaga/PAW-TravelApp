import {Component, OnInit} from '@angular/core';
import {ApiTripService} from "../services/api-trip.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {Trip} from "../model/trip";
import {ActivatedRoute, Router} from "@angular/router";

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
        this.currentPage = this.route.snapshot.queryParams['name'] | 1;
        this.getPageTrips(this.currentPage);
    }

    getPageTrips(page: number) {
        this.spinner.show();
        this.ts.getTripsForPage(this.currentPage).subscribe(
            data => {
                this.trips = data.trips;
                this.numberOfPages = data.maxPage;
                this.totalTrips = data.totalAmount;
            },
            err => {
                switch (err.status) {
                    case (400):
                        this.spinner.hide();
                        this.router.navigate(["/404"]);
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

}
