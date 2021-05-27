import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {Trip, TripStatus} from "../../model/trip";
import {ApiTripService} from "../../services/api-trip.service";
import {AuthService} from "../../services/auth/auth.service";
import {Router} from "@angular/router";
import {ApiSearchService} from "../../services/api-search.service";
import {Observable, Subject} from "rxjs";
import {debounceTime, distinctUntilChanged, filter, switchMap} from "rxjs/operators";
import {User} from "../../model/user";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DomSanitizer} from "@angular/platform-browser";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {TripRole} from "../../model/TripMember";
import {DateUtilService} from "../../services/date-util.service";
import {BsDatepickerConfig} from "ngx-bootstrap/datepicker";
import {TripInvitation} from "../../model/forms/TripInvitation";

declare var require: any;

@Component({
    selector: 'app-information',
    templateUrl: './information.component.html',
    styleUrls: ['./information.component.scss']
})
export class InformationComponent implements OnInit {

    @Input() trip: Trip;
    @Input() isAdmin: boolean;
    @Input() isMember: boolean;
    @Input() isCreator: boolean;
    @Input() completed: boolean;
    @Input() loggedUser: User;

    waitingConfirmation = true;

    canFinish: boolean;

    modalRef: BsModalRef;

    editTripModalRef: BsModalRef;
    editTripForm: FormGroup;

    searchTerm: string;
    userSearchList: Observable<User>;
    latestSearch = new Subject<string>();

    date: Date = new Date();
    startDate: Date;
    endDate: Date;

    bsConfig: Partial<BsDatepickerConfig> = Object.assign({}, {containerClass: 'theme-dark-blue'});

    showSuccessAlert;
    showErrorAlert;

    submitted: boolean;

    tripImage: any;
    loadingImage: boolean;
    hasImage: boolean;

    userLang: string;

    selectedFile: File;
    invalidFileExtension: boolean;
    invalidFileSize: boolean;
    validExtensions: string[] = ['jpeg', 'png', 'jpg'];
    maxImageSize: number = 5242880;

    // Webpack require for image fingerprinting
    tripDefaultImg = require('!!file-loader!../../../assets/images/trip-default-info.jpg').default;
    plusIcon = require('!!file-loader!../../../assets/icons/plus.png').default;
    editIcon = require('!!file-loader!../../../assets/icons/editar.png').default;
    deleteIcon = require('!!file-loader!../../../assets/icons/eliminar-small.png').default;

    constructor(private tripService: ApiTripService,
                private authService: AuthService,
                private router: Router,
                private searchService: ApiSearchService,
                private modalService: BsModalService,
                private sanitizer: DomSanitizer,
                private spinner: NgxSpinnerService,
                private formBuilder: FormBuilder,
                private dateUtils: DateUtilService) {

        this.userSearchList = this.latestSearch.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            filter(term => !!term),
            switchMap(term => this.searchService.searchInvitableUsersByName(term, this.trip.id)));
    }

    ngOnInit() {
/*
        this.isAdmin = !!this.trip.members.find(member => (member.role === TripRole.CREATOR || member.role === TripRole.ADMIN) &&
            member.user.id === this.loggedUser.id);
        this.isMember = !!(this.isAdmin || this.trip.members.find(member => member.user.id === this.loggedUser.id));
        this.isCreator = !!this.trip.members.find(member => (member.role === TripRole.CREATOR && member.user.id === this.loggedUser.id));
*/
        this.startDate = this.dateUtils.stringToDate(this.trip.startDate);
        this.endDate = this.dateUtils.stringToDate(this.trip.endDate);
        this.canFinish = this.canMarkAsCompleted();
        this.editTripForm = this.formBuilder.group({
            description: ['', [Validators.required, Validators.minLength(25), Validators.maxLength(400)]],
            tripName: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(25)]],
        });
        this.populateForm();
        if (this.trip != null) {
            if (!this.isAdmin && !this.isMember) {
                this.tripService.getPendingConfirmations(this.trip.id, this.authService.getLoggedUser().id).subscribe(
                    pendingConfirmations => {
                        this.waitingConfirmation = pendingConfirmations.some(p => p.trip.id === this.trip.id && p.user.id === this.authService.getLoggedUser().id);
                    }
                );
            }

            if (this.trip.imageURL) {
                this.loadingImage = true;
                this.tripService.getTripImage(this.trip.imageURL).subscribe(
                    data => {
                        const reader = new FileReader();
                        reader.onload = (e) => {
                            // @ts-ignore
                            this.tripImage = this.sanitizer.bypassSecurityTrustUrl(e.target.result);
                            this.loadingImage = false;
                            this.hasImage = true;
                        };
                        reader.readAsDataURL(new Blob([data]));
                    },
                    () => {
                        this.loadingImage = false;
                        this.hasImage = false;
                    }
                );
            }
            else {
                this.hasImage = false;
            }
        }
    }

    canMarkAsCompleted(): boolean {
        let now: Date = new Date();
        now.setHours(0, 0, 0, 0);
        return this.isCreator && now >= this.endDate;
    }

    populateForm() {
        this.editTripForm.get('tripName').setValue(this.trip.name);
        this.editTripForm.get('description').setValue(this.trip.description);
    }

    requestJoinTrip() {
        this.tripService.sendJoinRequest(this.trip.id, this.authService.getLoggedUser().id).subscribe(
            () => {
                this.waitingConfirmation = true;
            }
        );
    }

    exitTrip() {
        this.tripService.exitTrip(this.trip.id).subscribe(
            () => {
                this.router.navigate(["/user-trips"]);
            }
        );
    }

    get f() {
        return this.editTripForm.controls;
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
        this.tripService.inviteUserToTrip(this.trip.id, new TripInvitation(this.trip.id, user.id)).subscribe(
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

    openEditTripModal(template: TemplateRef<any>) {
        this.populateForm();
        this.editTripModalRef = this.modalService.show(template);
    }

    closeEditTripModal() {
        this.editTripModalRef.hide();
        this.resetEditTripForm();
    }


    submitEditTripForm() {
        this.submitted = true;
        if (this.editTripForm.invalid) {
            return;
        }
        let error = false;
        const formData = new FormData();
        if (!!this.selectedFile) {
            if (!this.validImgExtension()) {
                error = true;
                this.invalidFileExtension = true;
            }
            if (!this.validImgSize()) {
                error = true;
                this.invalidFileSize = true;
            }
            if (error) {
                return;
            }
            formData.append('image', this.selectedFile, this.selectedFile.name);
        }
        formData.append('tripName', this.editTripForm.get('tripName').value);
        formData.append('description', this.editTripForm.get('description').value);
        this.tripService.editTrip(formData, this.trip.id).subscribe(
            () => {
                window.location.reload();
            }
        );

    }

    resetEditTripForm() {
        this.submitted = false;
        this.selectedFile = null;
        this.editTripForm.reset();
    }

    onFileSelected(event) {
        this.selectedFile = event.target.files[0];
    }

    grantAdminRole(user: User) {
        this.tripService.grantAdminRole(this.trip.id, user.id).subscribe(
            () => {
                const index = this.trip.members.findIndex(member => member.user.id === user.id);
                this.trip.members[index].role = TripRole.ADMIN;
            }
        );
    }

    validImgExtension() {
        const extension = this.selectedFile.name.split('.')[1].toLowerCase();
        return this.validExtensions.includes(extension);
    }

    validImgSize() {
        return this.selectedFile.size <= this.maxImageSize;
    }

    deleteTrip() {
        this.tripService.deleteTrip(this.trip.id).subscribe(
            () => {
                this.router.navigate(["/home"]);
            }
        );
    }

    finishTrip() {
        this.tripService.finishTrip(this.trip.id).subscribe(
            () => {
                this.trip.status = TripStatus.COMPLETED;
                this.completed = true;
            }
        );
    }
}

