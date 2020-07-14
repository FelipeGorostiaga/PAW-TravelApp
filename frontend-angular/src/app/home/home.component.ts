import { Component, OnInit } from '@angular/core';
import {ApiUserService} from "../services/api-user.service";
import {Router} from "@angular/router";
import {Trip} from "../model/trip";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  trips: Trip[][];
  currentPage: number;

  constructor(private us: ApiUserService, private router: Router) { }

  ngOnInit() {
      this.currentPage = 0;
      this.us.getAllTrips().subscribe(
        res => {
            this.trips = res;
        },
        err => {
            this.trips = [[
                {
                    id: "1",
                    name: "mock trip",
                    description: "this is a test mock description",
                    startDate: "1997-06-16",
                    endDate: "1997-08-20"
                },
                {
                    id: "2",
                    name: "this is also a test",
                    description: "this is a test mock description 2",
                    startDate: "1997-06-16",
                    endDate: "1997-08-20"
                },
                {
                    id: "3",
                    name: "another test mother focker",
                    description: "this is a test mock description 3",
                    startDate: "1997-06-17",
                    endDate: "1997-08-20"
                },
                {
                    id: "4",
                    name: "another test mother fockerasd asd a",
                    description: "this is a test mock description 4",
                    startDate: "1997-06-17",
                    endDate: "1997-08-20"
                }],
                [
                    {
                        id: "1",
                        name: "second page",
                        description: "second page?",
                        startDate: "1997-06-16",
                        endDate: "1997-08-20"
                    },
                    {
                        id: "2",
                        name: "jejeje",
                        description: "this is a test mock description 2",
                        startDate: "1997-06-16",
                        endDate: "1997-08-20"
                    },
                    {
                        id: "3",
                        name: "asdasdasd",
                        description: "this is a test mock description 3",
                        startDate: "1997-06-17",
                        endDate: "1997-08-20"
                    },
                    {
                        id: "4",
                        name: "asdasdasdasd fuck da police",
                        description: "this is a test mock description 4",
                        startDate: "1997-06-17",
                        endDate: "1997-08-20"
                    }]
            ];
            alert("ERROR: couldn't get trips from server");
        }
    );
  }

  updatePage(newPage) {
      this.currentPage = newPage;
  }

}
