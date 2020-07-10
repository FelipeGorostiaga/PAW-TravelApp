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

    private registerForm: FormGroup;
    private submitted = false;
    private birthday: string;
    private password: string;
    private passwordRepeat: string;
    private email: string;
    private firstname: string;
    private lastname: string;
    private nationality: string;

    constructor(private http: HttpClient,
                private authService: AuthService,
                private router: Router,
                private formBuilder: FormBuilder) {
    }

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

    registerUser() {
        const formData = new UserForm(this.firstname, this.lastname, this.email, this.password, this.passwordRepeat,
        this. nationality, this.birthday);
        this.authService.register(formData).subscribe(
            res => {
              console.log(res);
              this.authService.login(new UserAuth(formData.email, formData.password))
                  .subscribe(
                      // tslint:disable-next-line:no-shadowed-variable
                      res => {
                        this.authService.setJwtToken(res);
                      },
                    error => {
                        alert("Error redirecting to login");
                      }
              );
              this.router.navigate(["/home"]);
            },
            err => {
              alert("Error registering user");
            }
        );
    }

    // convenience getter for easy access to form fields
    get f() { return this.registerForm.controls; }

    onSubmit() {
        this.submitted = true;
        if (this.registerForm.invalid) {
            return;
        }
        // display form values on success
        alert('SUCCESS!! :-)\n\n' + JSON.stringify(this.registerForm.value, null, 4));
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

