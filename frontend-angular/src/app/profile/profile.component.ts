import {Component, OnInit, TemplateRef} from '@angular/core';
import {User} from "../model/user";
import {ApiUserService} from "../services/api-user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../services/auth/auth.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {DomSanitizer} from "@angular/platform-browser";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MustMatch, validDate, ValidDate} from "../register/register.component";

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

    loggedUser: User;
    isProfileOwner: boolean;
    modalRef: BsModalRef;

    editProfileForm: FormGroup;

    user: User;

    loading: boolean;

    loadingImage;
    profilePicture;
    hasImage;

    submitted;

    acceptedFileTypes: string[] = ['jpeg', 'png'];

    constructor(private us: ApiUserService,
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
        this.loading = true;
        this.spinner.show();
        this.loggedUser = this.authService.getLoggedUser();
        const profileId = Number(this.route.snapshot.paramMap.get("id"));
        this.isProfileOwner = this.loggedUser.id === profileId;

        this.editProfileForm = this.formBuilder.group({
            imageUpload: ['', Validators.required],
            biography: ['', [Validators.required, Validators.maxLength(150)]],
        }, {
            validators: [validImgExtension('imageUpload')]
        });

        this.us.getUserById(profileId).subscribe(
            data => {
                this.user = data;
                this.loading = false;
                this.spinner.hide();
            },
            error => {
                console.log(error);
                this.spinner.hide();
                this.loading = false;
                this.router.navigate(['/404']);
            });

        this.us.getUserPicture(profileId).subscribe(
            data => {
                console.log(data);
                let objectURL = 'data:image/jpeg;base64,' + data.image;
                this.profilePicture = this.sanitizer.bypassSecurityTrustUrl(objectURL);
                this.loadingImage = false;
                this.hasImage = true;
            },
            error => {
                console.log(error);
                this.loadingImage = false;
                this.hasImage = false;
            }
        );
    }

    createImageFromBlob(image: Blob) {
        let reader = new FileReader();
        reader.addEventListener("load", () => {
            this.profilePicture = reader.result;
        }, false);

        if (image) {
            reader.readAsDataURL(image);
        }
    }

    openModal(template: TemplateRef<any>) {
        this.modalRef = this.modalService.show(template);
    }

    resetForm() {
        this.submitted = false;
        this.editProfileForm.reset();
    }

    closeModal() {
        this.modalRef.hide();
        this.resetForm();
    }

    submitEditProfile() {
        this.submitted = true;
        const values = this.editProfileForm.value;
        console.log(values);
        console.log(this.editProfileForm);
        if (this.editProfileForm.invalid) {
            console.log("returning");
            return;
        }
        console.log("NO ERRORS");

    }

    get f() {
        return this.editProfileForm.controls;
    }

}

export function validImgExtension(controlName: string) {
    return (formGroup: FormGroup) => {
        const control = formGroup.controls[controlName];
        if (control.errors) {
            return;
        }
        const extension = control.value.split('.')[1].toLowerCase();
        // set error on matchingControl if validation fails
        if (extension !== 'jpg' && extension !== 'png') {
            control.setErrors({invalidDate: true});
        } else {
            control.setErrors(null);
        }
    };
}
