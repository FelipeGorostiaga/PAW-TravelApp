import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Trip} from "../model/trip";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {ApiSearchService} from "../services/api-search.service";

@Component({
    selector: 'app-search-result',
    templateUrl: './search-result.component.html',
    styleUrls: ['./search-result.component.scss']
})
export class SearchResultComponent implements OnInit {

    nameInput: string;

    trips: Trip[];

    serverError: boolean;
    invalidInput: boolean;

    currentPage: number;
    numberOfPages: number;
    totalTrips: number;

    constructor(private router: Router,
                private route: ActivatedRoute,
                private spinner: NgxSpinnerService,
                private searchService: ApiSearchService) {
    }

    ngOnInit(): void {
        this.currentPage = this.route.snapshot.queryParams['page'] || 1;
        this.nameInput = this.route.snapshot.queryParams['name'];
        if (!this.nameInput) {
            this.invalidInput = true;
        }
        if (Number(this.currentPage)) {
            this.getPageTrips(this.currentPage);
        } else {
            this.navigateNotFound();
        }
    }

    updatePage(newPage) {
        if (newPage === this.currentPage) {
            return;
        }
        this.currentPage = newPage;
        this.getPageTrips(newPage);
    }


    getPageTrips(page: number) {
        this.spinner.show();
        const formData: FormData = new FormData();
        formData.append('name', this.nameInput);
        this.searchService.advancedSearch(formData, page).subscribe(
            data => {
                this.trips = data.trips;
                this.numberOfPages = data.maxPage;
                this.totalTrips = data.totalAmount;
                this.spinner.hide();
            },
            err => {
                this.serverError = true;
                this.spinner.hide();
            }
        );
    }

    navigateNotFound() {
        this.router.navigate(['/404']);
    }

}
