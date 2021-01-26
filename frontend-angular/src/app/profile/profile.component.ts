import {Component, ElementRef, OnInit, TemplateRef} from '@angular/core';
import {User} from "../model/user";
import {ApiUserService} from "../services/api-user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../services/auth/auth.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {DomSanitizer} from "@angular/platform-browser";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {error} from "util";

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

    loading: boolean;

    loadingImage;
    profilePicture;
    hasImage;

    submitted;

    imageError;
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
            biography: ['', Validators.maxLength(150)],
        }, {
            validators: [validImgExtension('imageUpload', this.validExtensions)]
        });

        this.userService.getUserById(profileId).subscribe(
            data => {
                this.user = data;
                this.editProfileForm.get('biography').setValue(this.user.biography);
            },
            error => {
                console.log(error);
                this.router.navigate(['/404']);
            });

        this.userService.getUserPicture(profileId).subscribe(
            data => {
                const reader = new FileReader();
                reader.onload = (e) => {
                    // @ts-ignore
                    this.profilePicture = this.sanitizer.bypassSecurityTrustUrl(e.target.result);
                    this.loadingImage = false;
                }
                reader.readAsDataURL(new Blob([data]));
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


    openModal(template: TemplateRef<any>) {
        this.editProfileForm.get('biography').setValue(this.user.biography);
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
        if (this.editProfileForm.invalid) {
            return;
        }
       /* const formDataBiography = new FormData();
        const formDataImage = new FormData();
        formDataBiography.append('biography', this.editProfileForm.get('biography').value);
        formDataImage.append('image', this.selectedFile, this.selectedFile.name);
        */
        const formData = new FormData();
        formData.append('biography', this.editProfileForm.get('biography').value);
        formData.append('image', this.selectedFile, this.selectedFile.name);
        this.userService.editProfile(formData, this.user.id).subscribe(
            data => {
                console.log("success");
                window.location.reload();
            },
            error => {
                console.log("error");
            }
        );

      /*  if (this.editProfileForm.get('biography').value) {
            this.userService.editBiography(formDataBiography, this.loggedUser.id).subscribe(
                data => {
                    console.log("biography updated successfully");
                },
                error => {
                    console.log(error);
                }
            );
        }
        if (this.selectedFile) {
            this.userService.editProfilePicture(formDataImage, this.loggedUser.id).subscribe(
                data => {
                    console.log("profile picture updated successfully");
                    window.location.reload();
                },
                error => {
                    console.log(error);
                }
            )
        }*/
    }

    get f() {
        return this.editProfileForm.controls;
    }

    onFileSelected(event) {
        console.log(event.target.files[0]);
        this.selectedFile = event.target.files[0];
/*
        const max_height = 15200;
        const max_width = 25600;
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
        reader.readAsDataURL(this.selectedFile);*/
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
