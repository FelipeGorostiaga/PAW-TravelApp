import {Component, ElementRef, NgZone, OnInit, ViewChild} from '@angular/core';
import {MapsAPILoader} from "@agm/core";
import {FormControl} from "@angular/forms";
// @ts-ignore
import {} from 'googlemaps';



@Component({
  selector: 'app-create-trip',
  templateUrl: './create-trip.component.html',
  styleUrls: ['./create-trip.component.scss']
})
export class CreateTripComponent implements OnInit {



  @ViewChild('search')
  public searchElement: ElementRef;

  height: number;
  width: number;
  zoom: number;


  submittedPlace: boolean;
  latitude: number;
  longitude: number;
  latlongs: any = [];
  searchControl: FormControl;

  tripStatus: string;


  constructor(private mapsAPILoader: MapsAPILoader, private  ngZone: NgZone) { }

  ngOnInit() {
    this.tripStatus = "Public";
    this.submittedPlace = false;
    this.zoom = 14;
    this.width = 1110;
    this.height = 400;
    this.searchControl = new FormControl();
    this.setCurrentPosition();

    this.mapsAPILoader.load().then(
        () => {
          const autocomplete = new google.maps.places.Autocomplete(this.searchElement.nativeElement,
              {
                types: [],
              });

          autocomplete.addListener('place_changed', () => {
            this.ngZone.run(() => {
              const place: google.maps.places.PlaceResult = autocomplete.getPlace();
              if (place.geometry === undefined || place.geometry == null) {
                return;
              }
              const latlong = {
                latitude: place.geometry.location.lat(),
                longitude: place.geometry.location.lng()
              };
              this.submittedPlace = true;
              this.latitude = place.geometry.location.lat();
              this.longitude = place.geometry.location.lng();
              this.latlongs.push(latlong);
            });
          });
        });
  }

  setCurrentPosition() {
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.latitude = position.coords.latitude;
        this.longitude = position.coords.longitude;
      });
    } else {
      this.latitude = -34.603722;
      this.longitude = -58.381592;
    }
  }

  changeTripStatus() {
    if (this.tripStatus === "Private") {
      this.tripStatus = "Public";
    } else {
      this.tripStatus = "Private";
    }
  }
}
