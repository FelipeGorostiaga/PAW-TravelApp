import {Component, Input, OnInit} from '@angular/core';
import {ApiUserService} from "../../services/api-user.service";
import {User} from "../../model/user";
import {TripInvitation} from "../../model/forms/TripInvitation";
import {Router} from "@angular/router";
import {ApiTripService} from "../../services/api-trip.service";

@Component({
    selector: 'app-invitations',
    templateUrl: './invitations.component.html',
    styleUrls: ['./invitations.component.scss']
})
export class InvitationsComponent implements OnInit {

    @Input() user: User;

    loading: boolean;

    serverError: boolean;
    alreadyRespondedError: boolean;
    tripCompleted: boolean;

    sentResponseAccept: boolean;
    sentResponseDeny: boolean;

    constructor(private userService: ApiUserService,
                private tripService: ApiTripService,
                private router: Router) {
    }

    ngOnInit(): void {
        this.loading = false;
        if (!this.user.invitations) {
            this.userService.getUserInvitations(this.user.invitationsURL).subscribe(
                data => {
                    this.user.invitations = data;
                }
            );
        }
    }

    respondInvitation(invitation: TripInvitation, accepted: boolean) {
        if (this.loading) {
            return;
        }
        this.loading = true;
        this.tripService.respondTripInvite(invitation.trip.id, invitation.token, accepted).subscribe(
            () => {
                const index = this.user.invitations.indexOf(invitation);
                this.user.invitations.splice(index, 1);
                if (accepted) {
                    this.sentResponseAccept = true;
                } else {
                    this.sentResponseDeny = true;
                }
                this.loading = false;
            },
            error => {
                const status = error.status;
                if (status === 410) {
                    this.alreadyRespondedError = true;
                }
                else if (status == 406) {
                    this.tripCompleted = true;
                    const index = this.user.invitations.indexOf(invitation);
                    this.user.invitations.splice(index, 1);
                }
                else {
                    this.serverError = true;
                }
                this.loading = false;
            }
        );

    }

    navigateProfileURL(id: number) {
        const url = 'profile/' + id;
        this.router.navigate([url]);
    }

}
