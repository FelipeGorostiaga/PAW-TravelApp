import {Component, ElementRef, NgZone, OnInit, ViewChild} from '@angular/core';
import {MapsAPILoader} from "@agm/core";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
// @ts-ignore
import {} from 'googlemaps';
import {Router} from "@angular/router";
import {ApiUserService} from "../services/api-user.service";
import {TripForm} from "../model/forms/trip-form";



@Component({
  selector: 'app-create-trip',
  templateUrl: './create-trip.component.html',
  styleUrls: ['./create-trip.component.scss']
})
export class CreateTripComponent implements OnInit {


  @ViewChild('search')
  public searchElement: ElementRef;
  searchControl: FormControl;

  zoom: number;
  latitude: number;
  longitude: number;
  latlongs: any = [];
  submittedPlace: boolean;
  tripStatus: string;

  tripForm: FormGroup;
  submitted = false;

  constructor(private mapsAPILoader: MapsAPILoader, private  ngZone: NgZone, private router: Router,
              private apiService: ApiUserService, private formBuilder: FormBuilder) { }

  ngOnInit() {

    this.tripForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30) ]],
      description: ['', [ Validators.required, Validators.minLength(25), Validators.maxLength(100) ]],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      placeInput: ['', Validators.required],
      isPrivate: ['']
    }, {
    });

    this.tripStatus = "Public";
    this.submittedPlace = false;
    this.zoom = 14;
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
              this.zoom = 16;
              this.latlongs.push(latlong);
              this.tripForm.get('placeInput').setValue(place.formatted_address);

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

  onSubmit() {
    const values = this.tripForm.value;
    this.submitted = true;
    if (this.tripForm.invalid) {
      return;
    }
    const formData = new TripForm(values.name, values.description, values.startDate, values.endDate, values.placeInput, values.isPrivate);
    this.apiService.createTrip(formData).subscribe(
        res => {
          console.log("Trip created successfully");
          console.log(res);
          alert('SUCCESS!!\n\n' + JSON.stringify(this.tripForm.value, null, 4));
        },
        err => {
          alert('SUCCESS!!\n\n' + JSON.stringify(this.tripForm.value, null, 4));
          console.log("Error creating trip");
        }
    );
  }

  onReset() {
    this.tripStatus = "Public";
    this.submitted = false;
    this.tripForm.reset();
  }

  // convenience getter for easy access to form fields
  get f() { return this.tripForm.controls; }
}


