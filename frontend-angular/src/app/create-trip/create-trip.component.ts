import {Component, ElementRef, NgZone, OnInit, ViewChild} from '@angular/core';
import {MapsAPILoader} from "@agm/core";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
// @ts-ignore
import {} from 'googlemaps';
import {Router} from "@angular/router";
import {TripForm} from "../model/forms/trip-form";
import {ApiTripService} from "../services/api-trip.service";
import {BsDatepickerConfig} from "ngx-bootstrap/datepicker";
import {DateUtilService} from "../services/date-util.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

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

    mapsErrorMessage: string;
    datesErrorMessage: string;

    bsConfig: Partial<BsDatepickerConfig> = Object.assign({}, {containerClass: 'theme-dark-blue', dateInputFormat: 'DD/MM/YYYY'});

    constructor(private mapsAPILoader: MapsAPILoader,
                private  ngZone: NgZone,
                private router: Router,
                private ts: ApiTripService,
                private formBuilder: FormBuilder,
                private dateUtilService: DateUtilService,
                private spinner: NgxSpinnerService) {
    }

    ngOnInit() {
        this.spinner.show();
        this.tripForm = this.formBuilder.group({
            name: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(15)]],
            description: ['', [Validators.required, Validators.minLength(25), Validators.maxLength(100)]],
            startDate: ['', Validators.required],
            endDate: ['', Validators.required],
            placeInput: ['', Validators.required],
            isPrivate: ['']
        }, {
            validators: validInterval('startDate', 'endDate')
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
                this.spinner.hide();
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
        if (!!this.longitude || !!this.latitude) {
            this.mapsErrorMessage = "Invalid google maps location";
            return;
        }
        const formData = new TripForm(values.name, values.description, this.dateUtilService.convertToDateString(values.startDate),
            this.dateUtilService.convertToDateString(values.endDate), values.placeInput, !!values.isPrivate, this.latitude, this.longitude);
        this.ts.createTrip(formData).subscribe(
            res => {
                const tripId = res.id;
                const tripUrl = "/trip/" + tripId;
                this.router.navigate([tripUrl]);
            },
            err => {
                if (err.status === 400) {
                    err.error.forEach(e => {
                        if (e.invalidField === 'dates') {
                            this.datesErrorMessage = e.message;
                        }
                        if (e.invalidField === 'googleMaps') {
                            this.mapsErrorMessage = e.message;
                        }
                    });
                }
            }
        );
    }

    onReset() {
        this.tripStatus = "Public";
        this.submitted = false;
        this.tripForm.reset();
        this.latitude = null;
        this.longitude = null;
    }

    get f() {
        return this.tripForm.controls;
    }

    closeMapsAlert() {
        this.mapsErrorMessage = null;
    }

    closeDateAlert() {
        this.datesErrorMessage = null;
    }
}

export function validInterval(startControlName: string, endControlName: string) {
    return (formGroup: FormGroup) => {
        const startControl = formGroup.controls[startControlName];
        const endControl = formGroup.controls[endControlName];
        if (startControl.errors || endControl.errors) {
            return;
        }
        let now = new Date();
        let startDate = startControl.value;
        let endDate = endControl.value;
        if (startDate < now || startDate > endDate) {
            startControl.setErrors({invalidInterval: true});
            endControl.setErrors({invalidInterval: true});
        } else {
            startControl.setErrors(null);
        }
    };
}


