import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ApiUserService} from "../services/api-user.service";

declare var require: any;

@Component({
    selector: 'app-about',
    templateUrl: './about.component.html',
    styleUrls: ['./about.component.scss']
})
export class AboutComponent implements OnInit {

    sent: boolean;
    form: FormGroup;
    globeImg = require('!!file-loader!../../assets/images/globe-big.png').default;
    submitted: boolean;

    constructor(private fb: FormBuilder, private userService: ApiUserService) {
    }

    ngOnInit() {
        this.form = this.fb.group({
            message: ['', [Validators.required, Validators.minLength(20), Validators.maxLength(400)]]
        });
        this.submitted = false;
    }

    submit() {
        if (this.form.invalid || this.submitted) {
            return;
        }
        this.sent = true;
    }

    get f() {
        return this.form.controls;
    }

    showForm() {
        this.form.reset();
        this.sent = false;
    }
}
