import {Component, Input, OnInit} from '@angular/core';
import {Trip} from "../../model/trip";
import {ApiUserService} from "../../services/api-user.service";
import {ApiTripService} from "../../services/api-trip.service";
import { Router } from '@angular/router';

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


  constructor(private ts: ApiTripService, private router: Router) { }

  ngOnInit() {
    this.getTripImage();
    this.usersAmount = this.trip.membersAmount;
  }

  getTripImage() {
    this.isImageLoading = true;
    this.ts.getTripImage(this.trip.id).subscribe(
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

  navigateToTrip() {
    const url = '/trip/' + this.trip.id;
    this.router.navigate([url])
  }
}
