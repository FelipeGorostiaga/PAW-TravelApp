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

    loading: boolean;
    accepted: boolean;

    // http errors
    serverError: boolean;
    alreadyResponded: boolean;
    tripCompleted: boolean;

    error: boolean;

    constructor(private router: Router,
                private route: ActivatedRoute,
                private ts: ApiTripService,
                private spinner: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.spinner.show();
        this.tripId = Number(this.route.snapshot.paramMap.get("id"));
        let token = this.route.snapshot.queryParams['token'];
        this.accepted = this.route.snapshot.queryParams['accepted'];
        if (!token || !this.accepted) {
            this.spinner.hide();
            this.router.navigate(['404']);
        } else {
            this.ts.respondJoinRequest(this.tripId, token, this.accepted).subscribe(
                data => {
                    this.error = false;
                    this.loading = false;
                    this.spinner.hide();
                },
                err => {
                    this.error = true;
                    let status = err.status;
                    switch (status) {
                        case 403:
                            this.router.navigate(['forbidden']);
                            break;
                        case 406:
                            this.tripCompleted = true;
                            break;
                        case 409:
                            this.alreadyResponded = true;
                            break;
                        case 404:
                            this.router.navigate(['404']);
                            break;
                        case 500:
                            this.serverError = true;
                            break;

                    }
                    this.spinner.hide();
                }
            );
        }
    }

}
