import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {FullTrip, TripStatus} from "../../model/trip";
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

@Component({
    selector: 'app-information',
    templateUrl: './information.component.html',
    styleUrls: ['./information.component.scss']
})
export class InformationComponent implements OnInit {

    @Input() trip: FullTrip;
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

    loading: boolean;

    showSuccessAlert;
    showErrorAlert;

    submitted: boolean;

    tripImage;
    loadingImage: boolean;
    hasImage: boolean;

    userLang: string;

    selectedFile: File;
    invalidFileExtension: boolean;
    invalidFileSize: boolean;
    validExtensions: string[] = ['jpeg', 'png', 'jpg'];
    maxImageSize: number = 5242880;

    exitTripTitle: string;
    exitTripMessage: string;

    deleteTripTitle: string;
    deleteTripMessage: string;

    finishTripTitle: string;
    finishTripMessage: string;

    requestJoinTitle: string;
    requestJoinMessage: string;

    makeAdminTitle: string;
    makeAdminMessage: string;

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
        this.isAdmin = !!this.trip.members.find(member => (member.role === TripRole.CREATOR || member.role === TripRole.ADMIN) &&
            member.user.id === this.loggedUser.id);
        this.isMember = !!(this.isAdmin || this.trip.members.find(member => member.user.id === this.loggedUser.id));
        this.isCreator = !!this.trip.members.find(member => (member.role === TripRole.CREATOR && member.user.id === this.loggedUser.id));
        // @ts-ignore
        this.userLang = (navigator.language || navigator.userLanguage).substr(0, 2);
        if (this.userLang === 'es') {
            this.exitTripTitle = "Salir del viaje";
            this.exitTripMessage = "Estas seguro que deseas salir de este viaje?";

            this.deleteTripTitle = "Borrar viaje";
            this.deleteTripMessage = "Estas seguro que quieres borrar este viaje?";

            this.makeAdminTitle = "Rol de administrador";
            this.makeAdminMessage = "Estas seguro que quieres otorgar permisos de administrador a este usuario?";

            this.finishTripTitle = "Terminar viaje";
            this.finishTripMessage = "Estas seguro que quieres terminar este viaje?";

            this.requestJoinTitle = "Pedir unirse";
            this.requestJoinMessage = "Estas seguro que quieres pedir unirte a este viaje?";
        } else {
            this.exitTripTitle = "Exit trip";
            this.exitTripMessage = "Are you sure you want to exit this trip?";

            this.deleteTripTitle = "Delete trip";
            this.deleteTripMessage = "Are you sure you want to delete this trip?";

            this.makeAdminTitle = "Administrator role";
            this.makeAdminMessage = "Are you sure you want to grant administrator role?";

            this.finishTripTitle = "End trip";
            this.finishTripMessage = "Are you sure you want to end this trip?";

            this.requestJoinTitle = "Ask to join";
            this.requestJoinMessage = "Are you sure you want to ask to join this trip?";
        }
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
                this.tripService.isWaitingTripConfirmation(this.trip.id, this.authService.getLoggedUser().id).subscribe(
                    data => {
                        this.waitingConfirmation = true;
                    },
                    error => {
                        this.waitingConfirmation = false;
                    }
                );
            }
            this.loadingImage = true;
            this.tripService.getTripImage(this.trip.id).subscribe(
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
                error => {
                    this.loadingImage = false;
                    this.hasImage = false;
                }
            );
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
            data => {
                this.waitingConfirmation = true;
            }
        );
    }

    exitTrip() {
        this.tripService.exitTrip(this.trip.id).subscribe(
            ok => {
                this.router.navigate(["/user-trips"]);
            }
            , error => {
                console.log(error);
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
            data => {
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
        this.tripService.grantAdminRole(this.trip, user).subscribe(
            ok => {
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
            ok => {
                this.router.navigate(["/home"]);
            },
            error => console.log(error)
        );
    }

    finishTrip() {
        this.tripService.finishTrip(this.trip.id).subscribe(
            res => {
                this.trip.status = TripStatus.COMPLETED;
            }
        );
    }
}

