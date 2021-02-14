import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserForm} from "../model/forms/user-form";
import {AuthService} from "../services/auth/auth.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DateUtilService} from "../services/date-util.service";
import {environment} from "../../environments/environment";
import {ErrorDTO} from "../model/ErrorDTO";


@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

    registerForm: FormGroup;
    submitted = false;

    bsConfig = Object.assign({}, {containerClass: 'theme-dark-blue'});

    errors: ErrorDTO[];

    constructor(private http: HttpClient,
                private authService: AuthService,
                private router: Router,
                private formBuilder: FormBuilder,
                private dateUtilService: DateUtilService) {
    }

    ngOnInit() {
        this.registerForm = this.formBuilder.group({
            firstName: ['',[ Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
            lastName: ['',[ Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
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

    // convenience getter for easy access to form fields
    get f() {
        return this.registerForm.controls;
    }

    onSubmit() {
        const values = this.registerForm.value;
        this.submitted = true;
        if (this.registerForm.invalid) {
            return;
        }
        let url = `${environment.frontEndURL}` + '/verify';
        const formData = new UserForm(values.firstName, values.lastName, values.email, values.password, values.confirmPassword,
            values.nationality, this.dateUtilService.convertToDateString(values.birthDate), values.customRadioInline1, url);

        this.authService.register(formData).subscribe(
            data => {
                console.log(data);
                this.router.navigate(['/verification']);
            },
            error => {
                console.error(error);
                this.errors = error;
                console.log(this.errors);
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


/*export function ValidDate(controlName: string) {
    return (formGroup: FormGroup) => {
        const control = formGroup.controls[controlName];
        if (control.errors) {
            return;
        }
        // set error on matchingControl if validation fails
        if (validDate(control.value)) {
            control.setErrors({invalidDate: true});
        } else {
            control.setErrors(null);
        }
    };
}

export function validDate(date: string): boolean {
    // First check for the pattern
    if (!/^\d{1,2}\/\d{1,2}\/\d{4}$/.test(date)) {
        return false;
    }
    // Parse the date parts to integers
    const parts = date.split("/");
    const month = parseInt(parts[0], 10);
    const day = parseInt(parts[1], 10);
    const year = parseInt(parts[2], 10);
    // Check the ranges of month and year
    if (year < 1000 || year > 3000 || month === 0 || month > 12) {
        return false;
    }
    const monthLength = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    // Adjust for leap years
    if (year % 400 === 0 || (year % 100 !== 0 && year % 4 === 0)) {
        monthLength[1] = 29;
    }
    // Check the range of the day
    return day > 0 && day <= monthLength[month - 1];
}*/

