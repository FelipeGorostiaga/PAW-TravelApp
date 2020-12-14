import {Component, Input, OnInit} from '@angular/core';
import {FullTrip, Trip} from "../../model/trip";
import {ApiTripService} from "../../services/api-trip.service";
import {AuthService} from "../../services/auth/auth.service";
import {Router} from "@angular/router";
import {ModalService} from "../../modal";
import {ApiSearchService} from "../../services/api-search.service";
import {Observable, Subject} from "rxjs";
import {debounceTime, distinct, filter, switchMap} from "rxjs/operators";
import {User} from "../../model/user";


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
    waitingConfirmation: boolean;

    searchTerm: string;
    userSearchResults: Observable<User>;
    latestSearch = new Subject<string>();


    constructor(private tripService: ApiTripService,
                private authService: AuthService,
                private router: Router,
                private modalService: ModalService,
                private searchService: ApiSearchService) {
    }

    ngOnInit() {
        if (this.trip != null) {
            if (!this.isAdmin && !this.isMember) {
                this.tripService.isWaitingTripConfirmation(this.trip.id, this.authService.getLoggedUser().id).subscribe(
                    data => {
                        console.log(data);
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
        this.userSearchResults = this.latestSearch.pipe(
            debounceTime(500),
            distinct(),
            filter(term => !!term),
            switchMap(term => this.searchService.searchUserByName(term)))
    }

    requestJoinTrip() {
        if (confirm("are you sure you want to request to join this trip?")) {
            this.tripService.sendJoinRequest(this.trip.id, this.authService.getLoggedUser().id).subscribe(
                data => {
                    this.waitingConfirmation = true;
                    console.log("Request to join trip sent");
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
                    console.log("You are no longer part of this trip");
                    this.isMember = false;
                    this.isAdmin = false;
                    this.router.navigate(["/user-trips"]);
                }
            );
        }
    }


    displaySearchUser(modalId: string) {
        this.modalService.open(modalId);
    }

    closeModal(modalId: string) {
        this.modalService.close(modalId);
    }

    newSearch(term: string) {
        this.latestSearch.next(term);
    }
}
