import {Component, OnInit, TemplateRef} from '@angular/core';
import {User} from "../model/user";
import {ApiUserService} from "../services/api-user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../services/auth/auth.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {DomSanitizer} from "@angular/platform-browser";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

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

    imageError;
    imageBase64;
    isImageSaved = false;
    selectedFile: File;
    validExtensions: string[] = ['jpeg', 'png', 'jpg'];

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
        this.loading = true;
        this.spinner.show();
        this.loggedUser = this.authService.getLoggedUser();
        const profileId = Number(this.route.snapshot.paramMap.get("id"));
        this.isProfileOwner = this.loggedUser.id === profileId;

        this.editProfileForm = this.formBuilder.group({
            imageUpload: ['', Validators.required],
            biography: ['', [Validators.required, Validators.maxLength(150)]],
        }, {
            validators: [validImgExtension('imageUpload', this.validExtensions)]
        });

        this.userService.getUserById(profileId).subscribe(
            data => {
                this.user = data;
            },
            error => {
                console.log(error);
                this.router.navigate(['/404']);
            });

        this.userService.getUserPicture(profileId).subscribe(
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
        this.loading = false;
        this.spinner.hide();
    }

    createImageFromBlob(image: Blob) {
        let reader = new FileReader();
        reader.addEventListener('load', () => {
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
            return;
        }
        const formData = new FormData();
        if (this.isImageSaved) {
            formData.append('imageBase64', this.imageBase64);
        }
        formData.append('biography', this.editProfileForm.get('biography').value);
        this.userService.editProfile(formData, this.loggedUser.id).subscribe(
            data => {
                console.log("successfully updated profile picture");
            },
            error => {
                console.log(error);
            }
        );
    }

    get f() {
        return this.editProfileForm.controls;
    }

    encodeImageFileAsURL(file) {
        let reader = new FileReader();
        reader.onloadend = function() {
            console.log('RESULT', reader.result)
        }
        reader.readAsDataURL(file);
    }

    onFileSelected(event) {
        const max_height = 15200;
        const max_width = 25600;
        console.log(event.target.files[0]);
        this.selectedFile = event.target.files[0];
        const reader = new FileReader();
        reader.onload = (e: any) => {
            const image = new Image();
            image.src = e.target.result;
            image.onload = rs => {
                const img_height = rs.currentTarget['height'];
                const img_width = rs.currentTarget['width'];
                console.log(img_height, img_width);
                if (img_height > max_height && img_width > max_width) {
                    this.imageError =
                        'Maximum dimentions allowed ' +
                        max_height +
                        '*' +
                        max_width +
                        'px';
                    return false;
                } else {
                    this.imageBase64 = e.target.result;
                    this.isImageSaved = true;
                }
            };
        };
        reader.readAsDataURL(this.selectedFile);
    }
}

export function validImgExtension(controlName: string, validExtensions: string[]) {
    return (formGroup: FormGroup) => {
        const control = formGroup.controls[controlName];
        if (control.errors) {
            return;
        }
        const extension = control.value.split('.')[1].toLowerCase();
        console.log(extension);
        if (!validExtensions.includes(extension)) {
            control.setErrors({invalidExtension: true});
        } else {
            control.setErrors(null);
        }
    };
}
