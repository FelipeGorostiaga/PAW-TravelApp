import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {Trip} from "../../model/trip";
import {User} from "../../model/user";
import {TripRole} from "../../model/TripMember";
import {ApiTripService} from "../../services/api-trip.service";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {debounceTime, distinctUntilChanged, filter, switchMap} from "rxjs/operators";
import {Observable, Subject} from "rxjs";

declare var require: any;

@Component({
    selector: 'app-members',
    templateUrl: './members.component.html',
    styleUrls: ['./members.component.scss']
})
export class MembersComponent implements OnInit {

    @Input() trip: Trip;
    @Input() isCreator: boolean;
    @Input() isAdmin: boolean;
    @Input() completed: boolean;

    showSuccessAlert: boolean;
    showErrorAlert: boolean;

    modalRef: BsModalRef;

    searchTerm: string;
    userSearchList: Observable<User>;
    latestSearch = new Subject<string>();

    shield = require('!!file-loader!../../../assets/icons/shield.png').default;
    bluePlus = require('!!file-loader!../../../assets/icons/blue-plus.png').default;

    constructor(private tripService: ApiTripService, private modalService: BsModalService) {
        this.userSearchList = this.latestSearch.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            filter(term => !!term),
            switchMap(term => this.tripService.searchInvitableUsers(term, this.trip.id)));
    }

    ngOnInit(): void {
    }

    grantAdminRole(user: User) {
        this.tripService.grantAdminRole(this.trip.id, user.id).subscribe(
            () => {
                const index = this.trip.members.findIndex(member => member.user.id === user.id);
                this.trip.members[index].role = TripRole.ADMIN;
            }
        );
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
        this.tripService.inviteUserToTrip(this.trip.id, {tripId: this.trip.id, userId: user.id}).subscribe(
            () => {
                this.showSuccessAlert = true;
            },
            () => {
                this.showErrorAlert = true;
            }
        );
        this.resetSearch();
    }

    resetSearch() {
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
