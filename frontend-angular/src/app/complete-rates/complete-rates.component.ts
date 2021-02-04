import {Component, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {ActivatedRoute, Router} from "@angular/router";
import {ApiUserService} from "../services/api-user.service";
import {Rate} from "../model/rate";


@Component({
    selector: 'app-complete-rates',
    templateUrl: './complete-rates.component.html',
    styleUrls: ['./complete-rates.component.scss']
})
export class CompleteRatesComponent implements OnInit {

    pendingRates: Rate[];

    constructor(private spinner: NgxSpinnerService,
                private route: ActivatedRoute,
                private router: Router,
                private userService: ApiUserService) {
    }

    ngOnInit() {
        this.spinner.show();
        const userId = Number(this.route.snapshot.paramMap.get("id"));
        this.userService.getUserPendingRates(userId).subscribe(
            res => {
                this.pendingRates = res;
                this.spinner.hide();
            },
            error => {
                this.spinner.hide();
                console.log(error);
            }
        );
    }

    submitRate($event: FormData) {
        // todo\
        console.log($event.get('id'));
        console.log($event.get('comment'));
        console.log($event.get('rating'));
    }
}
