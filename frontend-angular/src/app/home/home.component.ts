import {Component, OnInit} from '@angular/core';
import {ApiTripService} from "../services/api-trip.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  trips: any[][];
  currentPage: number;
  numberOfPages: number;
  tripsPerPage = 4;

  constructor(private ts: ApiTripService,
              private spinner: NgxSpinnerService) { }

  ngOnInit() {
      this.spinner.show();
      this.currentPage = 0;
      this.ts.getAllTrips().subscribe(
        res => {
            this.trips = this.chopList(res);
            let totalTrips = res.length;
            this.numberOfPages = Math.ceil(totalTrips / this.tripsPerPage );
            this.spinner.hide();
        },
        err => {
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

}
