import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {FullTrip} from "../../model/trip";
import {ApiTripService} from "../../services/api-trip.service";
import {AuthService} from "../../services/auth/auth.service";
import {Router} from "@angular/router";
import {ApiSearchService} from "../../services/api-search.service";
import {Observable, Subject} from "rxjs";
import {debounceTime, distinctUntilChanged, filter, switchMap, tap} from "rxjs/operators";
import {User} from "../../model/user";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";


@Component({
    selector: 'app-information',
    templateUrl: './information.component.html',
    styleUrls: ['./information.component.scss']
})
export class InformationComponent implements OnInit {

    @Input() trip: FullTrip;
    @Input() isAdmin: boolean;
    @Input() isMember: boolean;
    hasImage: boolean;
    tripImage: any;
    waitingConfirmation = true;

    modalRef: BsModalRef;

    searchTerm: string;
    userSearchList: Observable<User>;
    latestSearch = new Subject<string>();

    loading: boolean;

    showSuccessAlert;
    showErrorAlert;

    constructor(private tripService: ApiTripService,
                private authService: AuthService,
                private router: Router,
                private searchService: ApiSearchService,
                private modalService: BsModalService) {

        this.userSearchList = this.latestSearch.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            filter(term => !!term),
            tap(elem => console.log(elem)),
            switchMap(term => this.searchService.searchInvitableUsersByName(term, this.trip.id)));
    }

    ngOnInit() {
        this.loading = true;
        if (this.trip != null) {
            if (!this.isAdmin && !this.isMember) {
                this.tripService.isWaitingTripConfirmation(this.trip.id, this.authService.getLoggedUser().id).subscribe(
                    data => {
                        this.waitingConfirmation = true;
                    },
                    error => {
                        console.log(error);
                        this.waitingConfirmation = false;
                    }
                );
            }
            this.tripService.getTripImage(this.trip.id).subscribe(
                data => {
                    this.tripImage = data.image;
                    this.hasImage = true;
                },
                error => {
                    this.hasImage = false;
                }
            );
        }
        this.loading = false;
    }

    requestJoinTrip() {
        if (confirm("are you sure you want to request to join this trip?")) {
            this.tripService.sendJoinRequest(this.trip.id, this.authService.getLoggedUser().id).subscribe(
                data => {
                    this.waitingConfirmation = true;
                },
                error => {
                    console.log("Error sending request...");
                }
            );
        }
    }

    exitTrip() {
        if (confirm("Are you sure you want to leave this trip?")) {
            this.tripService.exitTrip(this.trip.id, this.authService.getLoggedUser().id).subscribe(
                () => {
                    this.isMember = false;
                    this.isAdmin = false;
                    this.router.navigate(["/user-trips"]);
                }
            );
        }
    }

    openModal(template: TemplateRef<any>) {
        this.modalRef = this.modalService.show(template);
    }

    closeModal() {
        this.modalRef.hide();
        this.resetSearch();
    }

    newSearch(term: string) {
        this.latestSearch.next(term);
    }

    sendInvite(user: User) {
        if (confirm("Invite user to trip?")) {
            this.tripService.inviteUserToTrip(this.trip.id, user.id).subscribe(
                next => {
                    this.showSuccessAlert = true;
                },
                error => {
                    this.showErrorAlert = true;
                }
            );
            this.resetSearch();
        }

    }

    resetSearch()  {
        this.searchTerm = "";
        this.latestSearch.next(" ");
    }

    closeSuccessAlert() {
        this.showSuccessAlert = false;
    }

    closeErrorAlert() {
        this.showErrorAlert = false;
    }

}
