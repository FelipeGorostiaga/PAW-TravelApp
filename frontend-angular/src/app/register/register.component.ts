import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserForm} from "../model/forms/user-form";
import {AuthService} from "../services/auth/auth.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DateUtilService} from "../services/date-util.service";
import {ErrorDTO} from "../model/ErrorDTO";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";


@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

    registerForm: FormGroup;
    submitted = false;

    invalidEmail: boolean;

    bsConfig = Object.assign({}, {containerClass: 'theme-dark-blue', dateInputFormat: 'DD/MM/YYYY'});

    errors: ErrorDTO[];

    constructor(private http: HttpClient,
                private authService: AuthService,
                private router: Router,
                private formBuilder: FormBuilder,
                private dateUtilService: DateUtilService,
                private spinner: NgxSpinnerService) {
    }

    ngOnInit() {
        this.registerForm = this.formBuilder.group({
            firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
            lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(20)]],
            confirmPassword: ['', Validators.required],
            acceptTerms: [false, Validators.requiredTrue],
            birthDate: ['', Validators.required],
            nationality: ['', Validators.required],
            customRadioInline1: ['', Validators.required]
        }, {
            validators: [MustMatch('password', 'confirmPassword'), validBirthday('birthDate')]
        });
    }

    get f() {
        return this.registerForm.controls;
    }

    onSubmit() {
        const values = this.registerForm.value;
        this.submitted = true;
        if (this.registerForm.invalid) {
            return;
        }
        const formData = new UserForm(values.firstName, values.lastName, values.email, values.password, values.confirmPassword,
            values.nationality, this.dateUtilService.convertToDateString(values.birthDate), values.customRadioInline1);

        this.spinner.show();
        this.authService.register(formData).subscribe(
            () => {
                this.invalidEmail = false;
                this.spinner.hide();
                this.router.navigate(['/verification']);
            },
            error => {
                console.log(error);
                this.spinner.hide();
                this.invalidEmail = true;
                this.errors = error;
            });
    }

    onReset() {
        this.submitted = false;
        this.registerForm.reset();
    }

}

// custom validator to check that two fields match
export function MustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
        const control = formGroup.controls[controlName];
        const matchingControl = formGroup.controls[matchingControlName];

        if (matchingControl.errors && !matchingControl.errors.mustMatch) {
            // return if another validator has already found an error on the matchingControl
            return;
        }
        // set error on matchingControl if validation fails
        if (control.value !== matchingControl.value) {
            matchingControl.setErrors({mustMatch: true});
        } else {
            matchingControl.setErrors(null);
        }
    };
}

export function validBirthday(controlName: string) {
    return (formGroup: FormGroup) => {
        const control = formGroup.controls[controlName];
        if (control.errors) {
            return;
        }
        let birthDate: Date = control.value;
        let now: Date = new Date();
        // set error on matchingControl if validation fails
        if (birthDate >= now) {
            control.setErrors({validBirthday: true});
        } else {
            control.setErrors(null);
        }
    };
}


