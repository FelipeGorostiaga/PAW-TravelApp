import {Component, Input, OnInit} from '@angular/core';
import {Trip} from "../../model/trip";
import {ApiUserService} from "../../services/api-user.service";
import {ApiTripService} from "../../services/api-trip.service";
import {Router} from '@angular/router';
import {DomSanitizer} from "@angular/platform-browser";
import {DateUtilService} from "../../services/date-util.service";

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

    startDate: Date;
    endDate: Date;

    userLang: string;

    constructor(private tripService: ApiTripService,
                private sanitizer: DomSanitizer,
                private router: Router,
                private dateUtil: DateUtilService) {
    }

    ngOnInit() {
        // @ts-ignore
        this.userLang = (navigator.language || navigator.userLanguage).substr(0, 2);
        this.startDate = this.dateUtil.stringToDate(this.trip.startDate);
        this.endDate = this.dateUtil.stringToDate(this.trip.endDate);
        this.loadingImage = true;

        this.tripService.hasImage(this.trip.id).subscribe();

        if (this.trip.hasImage) {
            this.tripService.getTripCardImage(this.trip.id).subscribe(
                data => {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        // @ts-ignore
                        this.tripImage = this.sanitizer.bypassSecurityTrustUrl(e.target.result);
                        this.loadingImage = false;
                        this.hasImage = true;
                    };
                    reader.readAsDataURL(new Blob([data]));
                },
                error => {
                    this.loadingImage = false;
                    this.hasImage = false;
                }
            );
        } else {
            this.loadingImage = false;
            this.hasImage = false;
        }


    }

    navigateToTrip() {
        const url = '/trip/' + this.trip.id;
        this.router.navigate([url]);
    }

}
