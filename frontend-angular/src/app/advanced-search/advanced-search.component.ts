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
        this.route.queryParams.subscribe(params => {
            this.currentPage = params['page'] || 1;
            this.nameInput = params['name'];
            this.placeInput = params['place'];

            const start = params['startDate'];
            const end = params['endDate'];

            if (start) {
                this.startDate = this.stringToDate(start, 'dd/MM/yyyy', '/');
            }
            if (end) {
                this.endDate = this.stringToDate(end, 'dd/MM/yyyy', '/');
            }

            const formData = this.getFormData();
            this.searchTrips(formData, this.currentPage);
        });
    }

    private removeSpecialCharacters() {
        if (this.nameInput && this.nameInput.length > 0) {
            if (AdvancedSearchComponent.containsSpecialCharacters(this.nameInput)) {
                this.nameInput = null;
            }
        }
        if (!!this.placeInput && this.placeInput.length > 0) {
            if (AdvancedSearchComponent.containsSpecialCharacters(this.placeInput)) {
                this.placeInput = null;
            }
        }
    }

    private stringToDate(_date, _format, _delimiter): Date {
        if (!_date) {
            return null;
        }
        let formatLowerCase = _format.toLowerCase();
        let formatItems = formatLowerCase.split(_delimiter);
        let dateItems = _date.split(_delimiter);
        let monthIndex = formatItems.indexOf("mm");
        let dayIndex = formatItems.indexOf("dd");
        let yearIndex = formatItems.indexOf("yyyy");
        let month = parseInt(dateItems[monthIndex]);
        month -= 1;
        return new Date(dateItems[yearIndex], month, dateItems[dayIndex]);
    }

    public getPageTripsWithFilters(page: number) {
        if (this.submitted) {
            return;
        }
        this.removeSpecialCharacters();
        //this.addQueryParams();

        this.spinner.show();
        this.submitted = true;
        this.serverError = false;

        const formData = this.getFormData();
        this.searchTrips(formData, page);
    }

    private searchTrips(formData: FormData, page) {
        this.searchService.advancedSearch(formData, page).subscribe(
            data => {
                this.trips = data.trips;
                this.numberOfPages = data.maxPage;
                this.totalTrips = data.totalAmount;
                this.searched = true;
                this.spinner.hide();
                this.submitted = false;
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

    private getFormData(): FormData {
        let formData = new FormData();
        if (this.startDate) {
            formData.append('startDate', this.dateUtil.convertToDateString(this.startDate));
        }
        if (this.endDate) {
            formData.append('endDate', this.dateUtil.convertToDateString(this.endDate));
        }
        if (this.placeInput) {
            formData.append('place', this.placeInput);
        }
        if (this.nameInput) {
            formData.append('name', this.nameInput);
        }
        return formData;
    }

    public updatePage(newPage) {
        if (this.currentPage === newPage) {
            return;
        }
        this.currentPage = newPage;

        const formData = this.getFormData();
        this.searchTrips(formData, newPage);
/*        this.router.navigate(['/advanced-search'], {
            queryParams: { page: newPage},
            queryParamsHandling: 'merge'
        });*/
    }

    private static containsSpecialCharacters(input: string): boolean {
        return input.includes('%') || input.includes('_');
    }

    private navigateNotFound() {
        this.router.navigate(["/404"]);
    }

    private addQueryParams() {
        let queryParams = {
            page: this.currentPage
        };
        if (this.nameInput) {
            queryParams['name'] = this.nameInput;
        }
        if (this.placeInput) {
            queryParams['place'] = this.placeInput;
        }
        if (this.startDate) {
            queryParams['startDate'] = this.dateUtil.convertToDateString(this.startDate);
        }
        if (this.endDate) {
            queryParams['endDate'] = this.dateUtil.convertToDateString(this.endDate);
        }
        this.router.navigate(['/advanced-search'], {
            queryParams: queryParams
        });
    }

}
