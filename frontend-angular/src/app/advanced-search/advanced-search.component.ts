import { Component, OnInit } from '@angular/core';
import {ApiSearchService} from "../services/api-search.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {BsDatepickerConfig} from "ngx-bootstrap/datepicker";

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

  bsConfig: Partial<BsDatepickerConfig> = Object.assign({}, {containerClass: 'theme-dark-blue', dateInputFormat: 'DD/MM/YYYY'});

  constructor(private searchService: ApiSearchService,
              private spinner: NgxSpinnerService) { }

  ngOnInit(): void {

    this.spinner.show();

    this.spinner.hide();
  }

    searchTrips() {
        console.log("search trips");
    }
}
