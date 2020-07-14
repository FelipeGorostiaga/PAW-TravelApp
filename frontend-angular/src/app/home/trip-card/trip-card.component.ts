import {Component, Input, OnInit} from '@angular/core';
import {Trip} from "../../model/trip";
import {ApiUserService} from "../../services/api-user.service";

@Component({
  selector: 'app-trip-card',
  templateUrl: './trip-card.component.html',
  styleUrls: ['./trip-card.component.scss']
})
export class TripCardComponent implements OnInit {

  @Input() trip: Trip;
  tripImage: any;
  usersAmount: number;
  isImageLoading: boolean;


  constructor(private us: ApiUserService) { }

  ngOnInit() {
    this.getUserNumber();
    this.getTripImage();
  }

  getUserNumber() {
    this.us.getTripUsersAmount(this.trip.id).subscribe(
        res => {
          this.usersAmount = res;
        },
        err => {
          this.usersAmount = 0;
        }
    );
  }

  getTripImage() {
    this.isImageLoading = true;
    this.us.getTripImage(this.trip.id).subscribe(
        res => {
          this.isImageLoading = false;
          this.createImageFromBlob(res.image);
        },
        error => {
          this.isImageLoading = false;
          console.log("Error loading trip image...");
        }
    );
  }

  createImageFromBlob(image: Blob) {
    const reader = new FileReader();
    reader.addEventListener("load", () => {
      this.tripImage = reader.result;
    }, false);

    if (image) {
      reader.readAsDataURL(image);
    }
  }

  tripClicked() {
    console.log("clicked trip" + this.trip.id);
  }
}
