import {Component, OnInit, TemplateRef} from '@angular/core';
import {ApiUserService} from "../services/api-user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../services/auth/auth.service";
import {User} from "../model/user";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {DomSanitizer} from "@angular/platform-browser";
import {forkJoin} from "rxjs";

declare var require: any;

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

    user: User;
    isProfileOwner: boolean;
    modalRef: BsModalRef;
    loading: boolean;

    selectedIndex: number;

    bioInput;

    editProfileForm: FormGroup;

    hasRates: boolean;

    loadingImage: boolean;
    profilePicture;
    hasImage: boolean;

    ratesError: boolean;

    submitted: boolean;

    imageError: string;
    selectedFile: File;
    invalidFileExtension: boolean;
    invalidFileSize: boolean;
    validExtensions: string[] = ['jpeg', 'png', 'jpg'];
    maxImageSize: number = 5242880;

    defaultProfileImg = require('!!file-loader!../../assets/images/profile-default.jpg').default;

    constructor(private userService: ApiUserService,
                private authService: AuthService,
                private router: Router,
                private route: ActivatedRoute,
                private spinner: NgxSpinnerService,
                private formBuilder: FormBuilder,
                private modalService: BsModalService,
                private sanitizer: DomSanitizer) {
    }

    ngOnInit() {
        const idx = this.route.snapshot.paramMap.get('index');
        this.selectedIndex = +idx || 0;
        this.spinner.show();
        this.loading = true;
        this.ratesError = false;
        this.loadingImage = true;

        const profileId = Number(this.route.snapshot.paramMap.get("id"));
        if (!profileId) {
            this.spinner.hide();
            this.navigateNotFound();
        }

        this.userService.getUser(profileId).subscribe(
            data => {
                this.user = data;
                this.isProfileOwner = this.authService.getLoggedUser().id === this.user.id;

                this.editProfileForm = this.formBuilder.group({
                    biography: [this.user.biography, Validators.maxLength(500)]
                });

                if (this.user.imageURL) {
                    this.userService.getUserPicture(this.user.imageURL).subscribe(
                        data => {
                            this.hasImage = true;
                            const reader = new FileReader();
                            reader.onload = (e) => {
                                // @ts-ignore
                                this.profilePicture = this.sanitizer.bypassSecurityTrustUrl(e.target.result);
                                this.loadingImage = false;
                                this.spinner.hide();
                            };
                            reader.readAsDataURL(new Blob([data]));
                            this.hasImage = true;
                        },

                        () => {
                            this.loadingImage = false;
                            this.hasImage = false;
                        }
                    );
                } else {
                    this.loadingImage = false;
                    this.hasImage = false;
                    this.spinner.hide();
                }

                // Profile owner --> rates, pending, tripsData
                if (this.isProfileOwner) {
                    const rates$ = this.userService.getUserRates(this.user.ratesURL);
                    const pendingRates$ = this.userService.getUserPendingRates(this.user.pendingRatesURL);
                    const tripsData$ = this.userService.getUserTripsData(this.user.tripsDataURL);
                    forkJoin([rates$, pendingRates$, tripsData$]).subscribe(
                        res => {
                            this.user.rates = res[0];
                            this.user.pendingRates = res[1];
                            this.user.tripsData = res[2];
                            this.calculateUserRate();
                        },
                        () => {
                            this.ratesError = true;
                        }
                    );
                } else {
                    const rates$ = this.userService.getUserRates(this.user.ratesURL);
                    const tripsData$ = this.userService.getUserTripsData(this.user.tripsDataURL);
                    forkJoin([rates$, tripsData$]).subscribe(
                        res => {
                            this.user.rates = res[0];
                            this.user.tripsData = res[1];
                            this.calculateUserRate();
                        },
                        () => {
                            this.ratesError = true;
                        }
                    );

                }
            },
            () => {
                this.spinner.hide();
                this.navigateNotFound();
            }
        );
    }

    switchTab(index: number) {
        this.selectedIndex = index;
    }

    navigateNotFound() {
        this.router.navigate(['/404']);
    }

    openModal(template: TemplateRef<any>) {
        this.editProfileForm.get('biography').setValue(this.user.biography);
        this.modalRef = this.modalService.show(template);
    }

    resetForm() {
        this.invalidFileSize = false;
        this.invalidFileExtension = false;
        this.submitted = false;
        this.editProfileForm.reset();
    }

    closeModal() {
        this.modalRef.hide();
        this.resetForm();
    }

    submitEditProfile() {
        this.submitted = true;
        if (this.editProfileForm.invalid) {
            return;
        }
        const formData = new FormData();
        let error = false;
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
        if (!!this.editProfileForm.get('biography').value) {
            formData.append('biography', this.editProfileForm.get('biography').value);
        }
        this.userService.editProfile(formData, this.user.id).subscribe(
            () => {
                window.location.reload();
            },
            error => {
                this.imageError = error.message;
            }
        );
    }

    private calculateUserRate() {
        let totalRate = 0;
        const len = this.user.rates.length;
        if (len == 0) {
            this.hasRates = false;
            return;
        }
        this.hasRates = true;
        this.user.rates.forEach(rate => totalRate += rate.rate);
        this.user.rating = totalRate / len;
    }

    get f() {
        return this.editProfileForm.controls;
    }

    onFileSelected(event) {
        this.selectedFile = event.target.files[0];
    }

    validImgExtension() {
        const extension = this.selectedFile.name.split('.')[1].toLowerCase();
        return this.validExtensions.includes(extension);
    }

    validImgSize() {
        return this.selectedFile.size <= this.maxImageSize;
    }

}
