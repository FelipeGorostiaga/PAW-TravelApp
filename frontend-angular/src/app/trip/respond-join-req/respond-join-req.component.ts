import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ApiTripService} from "../../services/api-trip.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

@Component({
    selector: 'app-respond-join-req',
    templateUrl: './respond-join-req.component.html',
    styleUrls: ['./respond-join-req.component.scss']
})
export class RespondJoinReqComponent implements OnInit {

    tripId: number;
    error: boolean;
    loading: boolean;
    accepted: boolean;

    constructor(private router: Router,
                private route: ActivatedRoute,
                private ts: ApiTripService,
                private spinner: NgxSpinnerService) {
    }


    ngOnInit(): void {
        this.spinner.show();
        this.loading = true;
        this.tripId = Number(this.route.snapshot.paramMap.get("id"));
        let token = this.route.snapshot.queryParams['token'];
        this.accepted = this.route.snapshot.queryParams['accepted'];
        console.log(token);
        console.log(this.accepted);

        if (!token || !this.accepted) {
            console.log("Error reading token or accepted");
            this.spinner.hide();
            this.error = true;
        } else {
            console.log(this.accepted);
            console.log(token);
            this.ts.respondJoinRequest(this.tripId, token, this.accepted).subscribe(
                data => {
                    this.error = false;
                    this.loading = false;
                    this.spinner.hide();
                },
                err => {
                    this.spinner.hide();
                    this.error = true;
                }
            );
        }
    }

}
