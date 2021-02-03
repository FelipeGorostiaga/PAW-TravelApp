import { Component, OnInit } from '@angular/core';
import {ApiUserService} from "../services/api-user.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {ActivatedRoute} from "@angular/router";
import {Rate} from "../model/rate";

@Component({
  selector: 'app-user-rates',
  templateUrl: './user-rates.component.html',
  styleUrls: ['./user-rates.component.scss']
})
export class UserRatesComponent implements OnInit {

  user;
  rates: Rate[];
  errorMessage;

  constructor(private userService: ApiUserService,
              private spinner: NgxSpinnerService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.spinner.show();
    const userId = Number(this.route.snapshot.paramMap.get("id"));
    console.log(userId);
    this.userService.getUserRates(userId).subscribe(
        res => {
            this.rates = res;
            this.spinner.hide();
        },
        error => {
          this.errorMessage = error.message;
          this.spinner.hide();
        }
    )

  }

}
