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

  constructor(private userService: ApiUserService) { }

  ngOnInit(): void {
    this.userService.getUserRating(this.rate.ratedUser.id).subscribe(
        data => {
          this.userRating = data;
        }
    );
  }

  submitRate() {
    const formData = new FormData();
    if (this.comment.length < 20) {
      this.showAlert = true;
      return;
    }
    formData.append('id', this.rate.id.toString());
    formData.append('rating',this.rating.toString());
    formData.append('comment', this.comment);
    this.rateSubmitEvent.emit(formData)
  }

  closeAlert() {
    this.showAlert = false;
  }
}
