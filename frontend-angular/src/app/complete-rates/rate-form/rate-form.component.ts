import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Rate} from "../../model/rate";
import {ApiUserService} from "../../services/api-user.service";

@Component({
    selector: 'app-rate-form',
    templateUrl: './rate-form.component.html',
    styleUrls: ['./rate-form.component.scss']
})
export class RateFormComponent implements OnInit {

    @Output() rateSubmitEvent = new EventEmitter();
    @Input() rate: Rate;

    rating: number = 0;
    comment: string = "";
    userRating: number;

    errorMessage = "Please write a review, at least 20 characters long";
    showAlert = false;

    constructor(private userService: ApiUserService) {
    }

    ngOnInit(): void {
        console.log(this.rate);
        this.userService.getUserRating(this.rate.ratedUser.id).subscribe(
            data => {
                console.log(data);
                this.userRating = data;
            }
        );
    }

    submitRate() {
        if (this.comment.length < 20) {
            this.showAlert = true;
            return;
        }

        this.rateSubmitEvent.emit({
            id: this.rate.id,
            rating: this.rating,
            comment: this.comment,
            rate: this.rate
        });
    }

    closeAlert() {
        this.showAlert = false;
    }
}
