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
    trips: Trip[][];
    currentPage: number;
    numberOfPages: number;
    tripsPerPage = 4;
    error: boolean;

    constructor(private router: Router,
                private route: ActivatedRoute,
                private spinner: NgxSpinnerService,
                private searchService: ApiSearchService) {
    }

    ngOnInit(): void {
        this.spinner.show();
        this.currentPage = 0;
        this.nameInput = this.route.snapshot.queryParams['name'];
        if (!this.nameInput) {
            this.error = true;
        }
        this.searchService.searchTripsByName(this.nameInput).subscribe(
            data => {
                this.trips = this.chopList(data);
                this.numberOfPages = Math.ceil(data.length / this.tripsPerPage);
                this.spinner.hide();
            },
            err => {
                this.error = true;
                this.spinner.hide();
            }
        );
    }


    chopList(arr: any) {
        const newarr = new Array();
        for (let i = 0; i < arr.length; i = i + this.tripsPerPage) {
            let tempArray = arr.slice(i, i + this.tripsPerPage);
            newarr.push(tempArray);
        }
        return newarr;
    }

    updatePage(newPage) {
        this.currentPage = newPage;
    }

}
