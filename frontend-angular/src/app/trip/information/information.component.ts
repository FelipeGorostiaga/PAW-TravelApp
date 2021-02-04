import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {FullTrip} from "../../model/trip";
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
import {validImgExtension} from "../../profile/profile.component";
import {TripRole} from "../../model/TripMember";
import {DateUtilService} from "../../services/date-util.service";
import {BsDatepickerConfig} from "ngx-bootstrap/datepicker";

@Component({
    selector: 'app-information',
    templateUrl: './information.component.html',
    styleUrls: ['./information.component.scss']
})
export class InformationComponent implements OnInit {

    @Input() trip: FullTrip;
    @Input() isAdmin: boolean;
    @Input() isMember: boolean;

    waitingConfirmation = true;

    modalRef: BsModalRef;

    editTripModalRef: BsModalRef;
    editTripForm: FormGroup;

    searchTerm: string;
    userSearchList: Observable<User>;
    latestSearch = new Subject<string>();

    date: Date = new Date();
    startDate: Date;
    endDate: Date;

    bsConfig: Partial<BsDatepickerConfig> = Object.assign({}, { containerClass: 'theme-dark-blue'});

    loading: boolean;

    showSuccessAlert;
    showErrorAlert;

    submitted: boolean;
    selectedFile: File;
    tripImage;
    loadingImage: boolean;
    hasImage: boolean;
    validExtensions: string[] = ['jpeg', 'png', 'jpg'];
    status;

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
        this.startDate = this.dateUtils.stringToDate(this.trip.startDate);
        this.endDate = this.dateUtils.stringToDate(this.trip.endDate);
        this.getTripStatus();
        this.loadingImage = true;
        
        // TODO: remove required image upload !!!!!!!!!!!!!!!
        this.editTripForm = this.formBuilder.group({
            imageUpload: ['', Validators.required],
            description: ['', [Validators.required, Validators.minLength(25), Validators.maxLength(500)]],
            tripName: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(25)]],
        }, {
            validators: [validImgExtension('imageUpload', this.validExtensions)]
        });
        this.populateForm();
        if (this.trip != null) {
            if (!this.isAdmin && !this.isMember) {
                this.tripService.isWaitingTripConfirmation(this.trip.id, this.authService.getLoggedUser().id).subscribe(
                    data => {
                        console.log("is waiting confirmation!");
                        this.waitingConfirmation = true;
                    },
                    error => {
                        this.waitingConfirmation = false;
                    }
                );
            }
            this.tripService.getTripImage(this.trip.id).subscribe(
                data => {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        // @ts-ignore
                        this.tripImage = this.sanitizer.bypassSecurityTrustUrl(e.target.result);
                        this.loadingImage = false;
                        this.hasImage = true;
                    }
                    reader.readAsDataURL(new Blob([data]));
                },
                error => {
                    this.loadingImage = false;
                    this.hasImage = false;
                }
            );
        }
    }

    populateForm() {
        this.editTripForm.get('tripName').setValue(this.trip.name);
        this.editTripForm.get('description').setValue(this.trip.description);
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
        this.populateForm();
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


    openEditTripModal(template: TemplateRef<any>) {
        this.editTripModalRef = this.modalService.show(template);
    }

    closeEditTripModal() {
        this.editTripModalRef.hide();
        this.resetEditTripForm();
    }


    submitEditTripForm() {
        this.submitted = true;
        if (this.editTripForm.invalid) {
            console.log("form is invalid");
            return;
        }
        const formData = new FormData();
        formData.append('tripName', this.editTripForm.get('tripName').value);
        formData.append('description', this.editTripForm.get('description').value);
        if (this.selectedFile) {
            formData.append('image', this.selectedFile, this.selectedFile.name);
        }
        this.tripService.editTrip(formData, this.trip.id).subscribe(
            data => {
                window.location.reload();
            },
            error => {
                console.log(error);
                // todo: check for error dtos
            }
        );

    }

    resetEditTripForm() {
        this.submitted = false;
        this.editTripForm.reset();
    }

    onFileSelected(event) {
        console.log(event.target.files[0]);
        this.selectedFile = event.target.files[0];
    }

    grantAdminRole(event: Event, user: User) {
        event.preventDefault();
        if (confirm("Are you sure you want to grant administrator role to " + user.firstname + " " + user.lastname + "?")) {
            this.tripService.grantAdminRole(this.trip, user).subscribe(
                ok => {
                    const index = this.trip.members.findIndex(member => member.user.id === user.id);
                    this.trip.members[index].role = TripRole.ADMIN;
                },
                error => {
                    console.log(error);
                }
            )
        }
    }

    private getTripStatus() {
        switch (this.trip.status) {
            case (0):
                this.status = "DUE";
                break;
            case (1):
                this.status = "IN PROGRESS";
                break;
            case (2):
                this.status = "COMPLETED";
                break;
        }
    }
}

