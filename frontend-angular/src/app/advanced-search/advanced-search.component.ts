import {Component, OnInit} from '@angular/core';
import {ApiSearchService} from "../services/api-search.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {BsDatepickerConfig} from "ngx-bootstrap/datepicker";
import {DateUtilService} from "../services/date-util.service";
import {Trip} from "../model/trip";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-advanced-search',
    templateUrl: './advanced-search.component.html',
    styleUrls: ['./advanced-search.component.scss']
})
export class AdvancedSearchComponent implements OnInit {


    startDate: Date;
    endDate: Date;
    nameInput;
    placeInput;

    numberOfPages: number;
    totalTrips: number;
    currentPage: number;

    trips: Trip[];

    submitted: boolean;

    serverError: boolean;

    searched: boolean;

    bsConfig: Partial<BsDatepickerConfig> = Object.assign({}, {containerClass: 'theme-dark-blue', dateInputFormat: 'DD/MM/YYYY'});

    constructor(private searchService: ApiSearchService,
                private spinner: NgxSpinnerService,
                private dateUtil: DateUtilService,
                private route: ActivatedRoute,
                private router: Router) {
    }

    ngOnInit(): void {
        this.currentPage = this.route.snapshot.queryParams['page'] || 1;
        if (!Number(this.currentPage)) {
            this.navigateNotFound();
        }
        this.getPageTripsWithFilters(this.currentPage);
    }

    public getPageTripsWithFilters(page: number) {
        if (this.submitted) {
            return;
        }

        if (!!this.nameInput) {
            if (this.containsSpecialCharacters(this.nameInput)) {
                this.nameInput = null;
            }
        }
        if (!!this.placeInput) {
            if (this.containsSpecialCharacters(this.placeInput)) {
                this.placeInput = null;
            }
        }

        this.spinner.show();
        this.submitted = true;
        this.serverError = false;

        let formData = new FormData();
        if (!!this.startDate) {
            formData.append('startDate', this.dateUtil.convertToDateString(this.startDate));
        }
        if (!!this.endDate) {
            formData.append('endDate', this.dateUtil.convertToDateString(this.endDate));
        }
        if (!!this.placeInput) {
            formData.append('place', this.placeInput);
        }
        if (!!this.nameInput) {
            formData.append('name', this.nameInput);
        }

        this.searchService.advancedSearch(formData, page).subscribe(
            data => {
                this.trips = data.trips;
                this.numberOfPages = data.maxPage;
                this.totalTrips = data.totalAmount;
                this.searched = true;
                this.spinner.hide();
                this.submitted = false;
                //this.addParamsToURL();
            },
            error => {
                switch (error.status) {
                    case 400:
                        this.navigateNotFound();
                        break;
                    case 500:
                        this.serverError = true;
                        break;
                }
                this.searched = true;
                this.submitted = false;
                this.spinner.hide();
            }
        );
    }


    public updatePage(newPage) {
        if (this.currentPage === newPage) {
            return;
        }
        this.currentPage = newPage;
        this.getPageTripsWithFilters(newPage);
    }

    private containsSpecialCharacters(input: string): boolean {
        return input.includes('%') || input.includes('_');
    }

    private navigateNotFound() {
        this.router.navigate(["/404"]);
    }

    /*    addParamsToURL() {
            let queryParams = {
                page: this.currentPage
            };
            if (!!this.nameInput) {
                queryParams["name"] = this.nameInput;
            }
            if (!!this.placeInput) {
                queryParams["place"] = this.placeInput;
            }
            if (!!this.startDate) {
                queryParams["startDate"] = this.dateUtil.convertToDateString(this.startDate);
            }
            if (!!this.endDate) {
                queryParams["endDate"] = this.dateUtil.convertToDateString(this.endDate);
            }
            this.router.navigate(['/advanced-search'], {
                queryParams: queryParams,
                queryParamsHandling: 'merge',
            });
        }*/

}
