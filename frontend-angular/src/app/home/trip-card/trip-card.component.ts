import {Component, Input, OnInit} from '@angular/core';
import {Trip} from "../../model/trip";
import {ApiUserService} from "../../services/api-user.service";
import {ApiTripService} from "../../services/api-trip.service";
import { Router } from '@angular/router';
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-trip-card',
  templateUrl: './trip-card.component.html',
  styleUrls: ['./trip-card.component.scss']
})
export class TripCardComponent implements OnInit {

  @Input() trip: Trip;

  hasImage;
  tripImage: any;
  loadingImage;

  constructor(private tripService: ApiTripService,
              private sanitizer: DomSanitizer,
              private router: Router) { }

  ngOnInit() {
    this.loadingImage = true;
    this.tripService.getTripCardImage(this.trip.id).subscribe(
        data => {
          const reader = new FileReader();
          reader.onload = (e) => {
            // @ts-ignore
            this.tripImage = this.sanitizer.bypassSecurityTrustUrl(e.target.result);
            this.loadingImage = false;
            this.hasImage = true;
          }
          reader.readAsDataURL(new Blob([data]));
        },
        error => {
          this.loadingImage = false;
          this.hasImage = false;
        }
    );
  }

  navigateToTrip() {
    const url = '/trip/' + this.trip.id;
    this.router.navigate([url])
  }

}
