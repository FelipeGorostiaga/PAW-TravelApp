import {Component, OnInit} from '@angular/core';
import {ApiSearchService} from "../services/api-search.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {BsDatepickerConfig} from "ngx-bootstrap/datepicker";
import {DateUtilService} from "../services/date-util.service";
import {Trip} from "../model/trip";

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
    tripsPerPage = 3;
    currentPage: number;

    trips: Trip[][];

    submitted: boolean;

    error: boolean;

    searched: boolean;

    bsConfig: Partial<BsDatepickerConfig> = Object.assign({}, {containerClass: 'theme-dark-blue', dateInputFormat: 'DD/MM/YYYY'});

    constructor(private searchService: ApiSearchService,
                private spinner: NgxSpinnerService,
                private dateUtil: DateUtilService) {
    }

    ngOnInit(): void {
        this.currentPage = 0;
        this.submitted = false;
        this.searched = false;
    }

    searchTrips() {
        if (this.submitted) return;
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
        if (!this.startDate && !this.endDate && !this.placeInput && !this.nameInput) {
            return;
        }
        this.error = false;
        this.spinner.show();
        this.submitted = true;
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
        this.searchService.advancedSearch(formData).subscribe(
            data => {
                this.trips = this.chopList(data);
                this.submitted = false;
                this.searched = true;
                this.spinner.hide();
            },
            error => {
                this.error = true;
                this.searched = true;
                this.submitted = false;
                this.spinner.hide();
            }
        );
    }


    chopList(arr: any) {
        const newarr = new Array();
        for (let i = 0; i < arr.length; i = i + this.tripsPerPage) {
            let tempArray = arr.slice(i, i + this.tripsPerPage)
            newarr.push(tempArray);
        }
        return newarr;
    }

    updatePage(newPage) {
        this.currentPage = newPage;
    }

    containsSpecialCharacters(input: string): boolean {
        return input.includes('%') || input.includes('_');
    }
}
