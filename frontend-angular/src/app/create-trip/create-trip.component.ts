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

    placeId: string;
    zoom: number;
    latitude: number;
    longitude: number;
    latlongs: any = [];
    submittedPlace: boolean;
    tripStatus: string;

    tripForm: FormGroup;
    submitted = false;

    userLang: string;

    mapsError: boolean;
    datesError: boolean;

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
        // @ts-ignore
        this.userLang = (navigator.language || navigator.userLanguage).substr(0,2);
        this.spinner.show();
        this.tripForm = this.formBuilder.group({
            name: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(15)]],
            description: ['', [Validators.required, Validators.minLength(25), Validators.maxLength(400)]],
            startDate: ['', Validators.required],
            endDate: ['', Validators.required],
            placeInput: ['', Validators.required],
            isPrivate: ['']
        }, {
            validators: validInterval('startDate', 'endDate')
        });
        this.tripStatus = this.userLang === 'en' ? "Public" : "Público";
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
                        this.placeId = place.place_id;
                        this.zoom = 16;
                        this.latlongs.push(latlong);
                        this.tripForm.get('placeInput').setValue(place.formatted_address);
                    });
                });
                this.spinner.hide();
            });
    }

    setCurrentPosition() {
        this.latitude = -34.603722;
        this.longitude = -58.381592;
        //production: google requires https for getCurrentPosition()
/*        if ('geolocation' in navigator) {
            navigator.geolocation.getCurrentPosition((position) => {
                this.latitude = position.coords.latitude;
                this.longitude = position.coords.longitude;
            });
        } else {
            this.latitude = -34.603722;
            this.longitude = -58.381592;
        }*/
    }

    changeTripStatus() {
        if (this.userLang === 'es') {
            if (this.tripStatus === "Privado") {
                this.tripStatus = "Público";
            }
            else {
                this.tripStatus = "Privado";
            }
        }
        else {
            if (this.tripStatus === "Private") {
                this.tripStatus = "Public";
            } else {
                this.tripStatus = "Private";
            }
        }
    }

    onSubmit() {
        const values = this.tripForm.value;
        this.submitted = true;
        if (this.tripForm.invalid) {
            return;
        }
        if (!this.longitude || !this.latitude || !this.placeId) {
            this.mapsError = true;
            return;
        }
        const formData = new TripForm(values.name, values.description, this.dateUtilService.convertToDateString(values.startDate),
            this.dateUtilService.convertToDateString(values.endDate), values.placeInput, !!values.isPrivate, this.latitude, this.longitude, this.placeId);
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
                            this.datesError = true;
                        }
                        if (e.invalidField === 'googleMaps') {
                            this.mapsError = true;
                        }
                    });
                }
            }
        );
    }

    onReset() {
        this.tripStatus = this.userLang === 'es' ? "Público" : "Public";
        this.submitted = false;
        this.tripForm.reset();
        this.latitude = null;
        this.longitude = null;
        this.placeId = null;
        this.latlongs = [];
        this.setCurrentPosition();
        this.submittedPlace = false;
    }

    get f() {
        return this.tripForm.controls;
    }

    closeMapsAlert() {
        this.mapsError = false;
    }

    closeDateAlert() {
        this.datesError = false;
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
        now.setHours(0,0,0,0);
        let startDate = startControl.value;
        let endDate = endControl.value;
        if (startDate < now || startDate > endDate) {
            startControl.setErrors({invalidInterval: true});
            endControl.setErrors({invalidInterval: true});
        } else {
            startControl.setErrors(null);
            endControl.setErrors(null);
        }
    };
}


