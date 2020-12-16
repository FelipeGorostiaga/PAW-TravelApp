import {Component, OnInit, TemplateRef} from '@angular/core';
import {User} from "../model/user";
import {ApiUserService} from "../services/api-user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../services/auth/auth.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {DomSanitizer} from "@angular/platform-browser";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MustMatch, ValidDate} from "../register/register.component";

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
        this.loadingImage = true;
        this.loading = true;
        this.spinner.show();
        this.loggedUser = this.authService.getLoggedUser();
        const profileId = Number(this.route.snapshot.paramMap.get("id"));
        this.isProfileOwner = this.loggedUser.id === profileId;

        this.editProfileForm = this.formBuilder.group({
            imageUpload: ['', Validators.required],
            biography: ['', Validators.required]
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

    closeModal() {
        this.modalRef.hide();
    }

    submitEditProfile() {

    }

}
