import {Component, ElementRef, Input, NgZone, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MapsAPILoader} from "@agm/core";
import {ActivatedRoute, Router} from "@angular/router";
import {ApiTripService} from "../../services/api-trip.service";
import {ActivityForm} from "../../model/forms/activity-form";
import {FullTrip} from "../../model/trip";
import {BsDatepickerConfig} from "ngx-bootstrap/datepicker";
import {DateUtilService} from "../../services/date-util.service";
import {ModalService} from "../../modal";
import {Activity} from "../../model/activity";

@Component({
    selector: 'app-activities',
    templateUrl: './activities.component.html',
    styleUrls: ['./activities.component.scss']
})
export class ActivitiesComponent implements OnInit {

    @ViewChild('search') public searchElement: ElementRef;
    searchControl: FormControl;

    @Input() trip: FullTrip;
    @Input() isAdmin: boolean;
    @Input() isMember: boolean;

    zoom: number;
    latitude: number;
    longitude: number;
    latlongs: any = [];
    submittedPlace: boolean;
    activityForm: FormGroup;
    submitted = false;

    isEmpty: boolean;
    bsConfig: Partial<BsDatepickerConfig> = Object.assign({}, {containerClass: 'theme-dark-blue', dateInputFormat: 'DD/MM/YYYY'});

    constructor(private ngZone: NgZone,
                private router: Router,
                private ts: ApiTripService,
                private formBuilder: FormBuilder,
                private route: ActivatedRoute,
                private dateUtilService: DateUtilService,
                private modalService: ModalService,
                private mapsAPILoader: MapsAPILoader,
                private dateUtil: DateUtilService) {
    }

    ngOnInit() {
        this.activityForm = this.formBuilder.group({
            name: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(60)]],
            category: ['', Validators.required],
            startDate: ['', Validators.required],
            endDate: ['', Validators.required],
            placeInput: ['', Validators.required]
        }, {
            validators: InvalidInterval('startDate', 'endDate', this.trip.activities, this.dateUtil.stringToDate(this.trip.startDate),
                this.dateUtil.stringToDate(this.trip.endDate))
        });
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
                        this.activityForm.get('placeInput').setValue(place.formatted_address);
                    });
                });
            });

    }

    createActivity() {
        const values = this.activityForm.value;
        this.submitted = true;
        if (this.activityForm.invalid) {
            return;
        }
        let form = new ActivityForm(values.name, values.category, values.placeInput, this.latitude, this.longitude, this.dateUtilService.convertToDateString(values.startDate),
            this.dateUtilService.convertToDateString(values.endDate));
        console.log(JSON.stringify(form));
        this.ts.createTripActivity(this.trip.id, form).subscribe(
            data => {
                this.trip.activities.push(data);
                this.closeModal('custom-modal-1');
            },
            error => {
                console.log(error);
            }
        );
    }

    openModal(id: string) {
        this.modalService.open(id);
    }

    closeModal(id: string) {
        this.modalService.close(id);
        this.resetFormData();
    }

    private resetFormData() {
        this.activityForm.reset();
        this.submitted = false;
        this.submittedPlace = false;
        this.latlongs = [];
        this.zoom = 14;
        this.setCurrentPosition();
    }

    get f() {
        return this.activityForm.controls;
    }

    private setCurrentPosition() {
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

    deleteActivity(activity: Activity) {
        this.ts.deleteTripActivity(this.trip.id, activity.id).subscribe(
            res => {
                let index = this.trip.activities.indexOf(activity);
                this.trip.activities.splice(index, 1);
            },
            error => {
                console.log(error);
            }
        );
    }
}

// custom validator to check that two fields match
export function InvalidInterval(sControl: string, eControl: string, activities: Activity[], tripSDate: Date, tripEDate: Date) {
    return (formGroup: FormGroup) => {
        const startControl = formGroup.controls[sControl];
        const endControl = formGroup.controls[eControl];
        if (startControl.errors || endControl.errors) {
            return;
        }
        let now: Date = new Date();
        now.setHours(0,0,0,0)
        let actSDate: Date = startControl.value;
        actSDate.setHours(0,0,0,0)
        let actEDate: Date = endControl.value;
        actEDate.setHours(0,0,0,0)
        if ((actSDate < now) || (actSDate > actEDate) || (actSDate < tripSDate) || (actEDate > tripEDate) || hasActivityConflict(activities, actSDate, actEDate)) {
            startControl.setErrors({invalidInterval: true});
            endControl.setErrors({invalidInterval: true});
        } else {
            startControl.setErrors(null);
        }
    };
}

export function hasActivityConflict(activities: Activity[], startDate: Date, endDate: Date): boolean {
    activities.forEach(
        activity => {
            let asdate = activity.startDate;
            let sday = Number(asdate.slice(0, 2));
            let smonth = Number(asdate.slice(3, 5)) - 1;
            let syear = Number(asdate.slice(6, 10));
            let aSDate = new Date(Number(syear), Number(smonth), Number(sday));
            aSDate.setHours(0,0,0,0);
            let aedate = activity.endDate;
            let eday = Number(aedate.slice(0, 2));
            let emonth = Number(aedate.slice(3, 5)) - 1;
            let eyear = Number(aedate.slice(6, 10));
            let eSDate = new Date(Number(eyear), Number(emonth), Number(eday));
            eSDate.setHours(0,0,0,0);
            if ((startDate <= aSDate && endDate >= eSDate) || (startDate >= aSDate && endDate <= eSDate) || (startDate >= aSDate && endDate > eSDate)
                || (startDate <= aSDate && endDate > aSDate)) {
                return true;
            }
        }
    );
    return false;
}
