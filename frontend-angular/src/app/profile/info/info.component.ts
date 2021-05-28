import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {User} from "../../model/user";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserProfile} from "../../model/UserProfile";
import {ApiUserService} from "../../services/api-user.service";
import {AuthService} from "../../services/auth/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {DomSanitizer} from "@angular/platform-browser";

declare var require: any;

@Component({
    selector: 'app-info',
    templateUrl: './info.component.html',
    styleUrls: ['./info.component.scss']
})
export class InfoComponent implements OnInit {

    @Input() user: User;
    @Input() isProfileOwner: boolean;

    modalRef: BsModalRef;

    bioInput;

    editProfileForm: FormGroup;

    userProfile: UserProfile;
    userRate: number;
    hasRates: boolean;

    loadingImage: boolean;
    profilePicture;
    hasImage: boolean;

    submitted: boolean;

    imageError: string;
    selectedFile: File;
    invalidFileExtension: boolean;
    invalidFileSize: boolean;
    validExtensions: string[] = ['jpeg', 'png', 'jpg'];
    maxImageSize: number = 5242880;

    defaultProfileImg = require('!!file-loader!../../../assets/images/profile-default.jpg').default;

    constructor(private userService: ApiUserService,
                private authService: AuthService,
                private router: Router,
                private route: ActivatedRoute,
                private spinner: NgxSpinnerService,
                private sanitizer: DomSanitizer,
                private modalService: BsModalService,
                private formBuilder: FormBuilder) {
    }

    ngOnInit() {

        this.submitted = false;
        this.loadingImage = true;

        this.editProfileForm = this.formBuilder.group({
            biography: [this.user.biography, Validators.maxLength(500)],
        });

        if (this.user.imageURL) {
            this.userService.getUserPicture(this.user.id).subscribe(
                data => {
                    this.hasImage = true;
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        // @ts-ignore
                        this.profilePicture = this.sanitizer.bypassSecurityTrustUrl(e.target.result);
                        this.loadingImage = false;
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
        }

    }

    /* const rates$ = this.userService.getUserRates(this.user.ratesURL);
     const pendingRates$ = this.userService.getUserRates(this.user.pendingRatesURL);
*/

    /*                let user$ = this.userService.getUser(userId);
                    let rating$ = this.userService.getUserRating(userId);
                    let rates$ = this.userService.getUserRates(userId);

                    forkJoin([user$ , rating$, rates$]).subscribe(
                        data => {
                            this.user = data[0];
                            this.userRating = data[1];
                            this.rates = data[2];
                            this.spinner.hide();
                        },
                        () => {
                            this.spinner.hide();
                            this.error = true;
                        }
                    );*/


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
