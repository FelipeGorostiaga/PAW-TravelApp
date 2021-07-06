import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Rate} from "../../../model/rate";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-rate-form-tile',
    templateUrl: './rate-form-tile.component.html',
    styleUrls: ['./rate-form-tile.component.scss']
})
export class RateFormTileComponent implements OnInit {

    @Output() rateSubmitEvent = new EventEmitter();
    @Input() rate: Rate;

    rating: number = 0;
    comment: string = "";
    userRating: number;

    errorMessage = "";
    showAlert = false;

    rateForm: FormGroup;

    submitted: boolean;

    constructor(private fb: FormBuilder) {
    }

    ngOnInit(): void {
        this.rateForm = this.fb.group({
            review: ['', [Validators.required, Validators.minLength(20), Validators.maxLength(140)]]
        });
    }

    get f() {
        return this.rateForm.controls;
    }

    submitRate() {
        this.submitted = true;
        if (this.rateForm.invalid) {
            return;
        }
        this.rateSubmitEvent.emit({
            id: this.rate.id,
            rating: this.rating,
            comment: this.comment,
            rate: this.rate
        });
    }

}
