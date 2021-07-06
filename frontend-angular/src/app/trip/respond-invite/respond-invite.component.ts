import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ApiTripService} from "../../services/api-trip.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

@Component({
    selector: 'app-respond-invite',
    templateUrl: './respond-invite.component.html',
    styleUrls: ['./respond-invite.component.scss']
})
export class RespondInviteComponent implements OnInit {

    tripId: number;
    error: boolean;
    loading: boolean;
    accepted: boolean;
    tripName: string = "";

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
        if (!token || !this.accepted) {
            this.spinner.hide();
            this.error = true;
        } else {
            this.ts.respondTripInvite(this.tripId, token, this.accepted).subscribe(
                data => {
                    this.tripName = data.name;
                    this.error = false;
                    this.loading = false;
                    this.spinner.hide();
                },
                () => {
                    this.loading = false;
                    this.spinner.hide();
                    this.error = true;
                }
            );
        }
    }

}
