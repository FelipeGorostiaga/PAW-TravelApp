import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserAuth} from "../model/user-auth";
import {UserForm} from "../model/forms/user-form";
import {AuthService} from "../services/auth/auth.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

    registerForm: FormGroup;
    submitted = false;

    constructor(private http: HttpClient,
                private authService: AuthService,
                private router: Router,
                private formBuilder: FormBuilder) {
    }

    // TODO: validate date

    ngOnInit() {
        this.registerForm = this.formBuilder.group({
            firstName: ['', Validators.required],
            lastName: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]],
            confirmPassword: ['', Validators.required],
            acceptTerms: [false, Validators.requiredTrue],
            birthDate: ['', Validators.required],
            nationality: ['', Validators.required]
        }, {
            validator: MustMatch('password', 'confirmPassword')
        });
    }

    // convenience getter for easy access to form fields
    get f() { return this.registerForm.controls; }

    onSubmit() {
        const values = this.registerForm.value;
        this.submitted = true;
        if (this.registerForm.invalid) {
            return;
        }
        const formData = new UserForm(values.firstName, values.lastName, values.email,
            values.password, values.confirmPassword, values.nationality, values.birthDate);
        this.authService.register(formData).subscribe(
            res => {
                    console.log(res);
                    this.authService.login(new UserAuth(formData.email, formData.password)).subscribe(
                        // tslint:disable-next-line:no-shadowed-variable
                        res => {
                            this.authService.setJwtToken(res);
                            this.router.navigate(["/home"]);
                        },
                        error => {
                            alert("Error redirecting to login");
                        }
                    );
            },
            err => {
                alert("Error registering user");
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
            matchingControl.setErrors({ mustMatch: true });
        } else {
            matchingControl.setErrors(null);
        }
    };
}

