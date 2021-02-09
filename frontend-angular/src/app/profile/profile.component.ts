import {Component, OnInit, TemplateRef} from '@angular/core';
import {User} from "../model/user";
import {ApiUserService} from "../services/api-user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../services/auth/auth.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {DomSanitizer} from "@angular/platform-browser";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserProfile} from "../model/UserProfile";

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

    loggedUser: User;
    isProfileOwner: boolean;
    modalRef: BsModalRef;

    bioInput;

    editProfileForm: FormGroup;

    user: User;
    userProfile: UserProfile;
    userRate: number;
    hasRates: boolean;

    loading: boolean;

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
        this.spinner.show();
        this.submitted = false;
        this.loadingImage = true;
        this.loading = true;
        this.loggedUser = this.authService.getLoggedUser();
        const profileId = Number(this.route.snapshot.paramMap.get("id"));
        if (!profileId) {
            this.router.navigate(['/404']);
        }
        this.isProfileOwner = this.loggedUser.id === profileId;
        this.editProfileForm = this.formBuilder.group({
            biography: ['', Validators.maxLength(500)],
        });

        this.userService.getUserProfileData(profileId).subscribe(
            data => {
                this.userProfile = data;
                this.user = data.user;
                this.editProfileForm.get('biography').setValue(this.userProfile.user.biography);
                this.calculateUserRate();
                this.spinner.hide();
            },
            error => {
                console.log(error);
                this.spinner.hide();
            }
        )

        this.userService.getUserPicture(profileId).subscribe(
            data => {
                const reader = new FileReader();
                reader.onload = (e) => {
                    // @ts-ignore
                    this.profilePicture = this.sanitizer.bypassSecurityTrustUrl(e.target.result);
                    this.loadingImage = false;
                };
                reader.readAsDataURL(new Blob([data]));
                this.hasImage = true;
            },
            error => {
                this.loadingImage = false;
                this.hasImage = false;
            }
        );
        this.loading = false;
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
            console.log("Form in invalid");
            return;
        }
        const formData = new FormData();
        let error = false;
        if (!!this.selectedFile) {
            console.log("There is a selected file");
            console.log(this.selectedFile);
            if (!this.validImgExtension()) {
                console.log("Invalid image extension");
                error = true;
                this.invalidFileExtension = true;
            }
            if (!this.validImgSize()) {
                console.log("Invalid image size");
                error = true;
                this.invalidFileSize = true;
            }
            if (error) {
                console.log("There is an error");
                return;
            }
            formData.append('image', this.selectedFile, this.selectedFile.name);
        }
        formData.append('biography', this.editProfileForm.get('biography').value);
        console.log(formData.get('biography'));
        console.log(formData.get('image'));
        this.userService.editProfile(formData, this.user.id).subscribe(
            data => {
                window.location.reload();
            },
            error => {
                console.log(error);
                this.imageError = error.message;
            }
        );
    }

    get f() {
        return this.editProfileForm.controls;
    }

    onFileSelected(event) {
        console.log(event.target.files[0]);
        this.selectedFile = event.target.files[0];
    }

    private calculateUserRate() {
        let totalRate = 0;
        const len = this.userProfile.rates.length;
        if (len == 0) {
            this.hasRates = false;
            return;
        }
        this.hasRates = true;
        this.userProfile.rates.forEach(rate => totalRate += rate.rate);
        this.userRate = Math.round(totalRate / len);
    }

    validImgExtension() {
        const extension = this.selectedFile.name.split('.')[1].toLowerCase();
        console.log(extension);
        return this.validExtensions.includes(extension);
    }

    validImgSize() {
        console.log(this.selectedFile.size);
        return this.selectedFile.size <= this.maxImageSize;
    }
}
