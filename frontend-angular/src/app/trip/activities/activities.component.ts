import {Component, ElementRef, Input, NgZone, OnInit, ViewChild} from '@angular/core';
import {ModalService} from "../../modal";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MapsAPILoader} from "@agm/core";
import {ActivatedRoute, Router} from "@angular/router";
import {ApiTripService} from "../../services/api-trip.service";
import {ActivityForm} from "../../model/forms/activity-form";
import {FullTrip} from "../../model/trip";
import {BsDatepickerConfig} from "ngx-bootstrap/datepicker";

@Component({
  selector: 'app-activities',
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.scss']
})
export class ActivitiesComponent implements OnInit {

  @Input() trip: FullTrip;
  @Input() isAdmin: boolean;
  @ViewChild('search')

  public searchElement: ElementRef;
  searchControl: FormControl;

  zoom: number;
  latitude: number;
  longitude: number;
  latlongs: any = [];
  submittedPlace: boolean;
  activityForm: FormGroup;
  submitted = false;

  isEmpty: boolean;
  bsConfig: Partial<BsDatepickerConfig> = Object.assign({}, { containerClass: 'theme-dark-blue', dateInputFormat: 'DD/MM/YYYY' });

  constructor(private modalService: ModalService, private mapsAPILoader: MapsAPILoader, private  ngZone: NgZone, private router: Router,
              private ts: ApiTripService, private formBuilder: FormBuilder, private route: ActivatedRoute) { }

  ngOnInit() {
    this.activityForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(60)]],
      category: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      placeInput: ['', Validators.required]
    }, {
      validators: InvalidDate('startDate', 'endDate')
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
      console.log("form is invalid");
      console.log(JSON.stringify(values));
      console.log(this.activityForm.errors);
      return;
    }
    let startDateString = this.convertToDateString(values.startDate);
    let endDateString = this.convertToDateString(values.endDate);
    let form = new ActivityForm(values.name, values.category, values.placeInput, startDateString, endDateString);
    console.log(JSON.stringify(form));
    this.ts.createTripActivity(this.trip.id, form).subscribe(
            data => {
              console.log(data);
              this.trip.activities.push(data);
              console.log("Added new activity to trip!");
              this.closeModal('custom-modal-1');
            },
            error => {
              console.log(error);
            }
        );
  }

  convertToDateString(date): string {
    let dateString = "";
    let day = date.getDate()
    let month = date.getMonth() + 1
    let year = date.getFullYear()
    if(month < 10){
      dateString = `${day}/0${month}/${year}`;
    }else{
      dateString = `${day}/${month}/${year}`;
    }
    return dateString;
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

  get f() { return this.activityForm.controls; }

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
}

// custom validator to check that two fields match
export function InvalidDate(startDateControlName: string, endDateControlName: string) {
  return (formGroup: FormGroup) => {
    const startControl = formGroup.controls[startDateControlName];
    const endControl = formGroup.controls[endDateControlName];
    if (startControl.errors || endControl.errors) {
      // return if another validator has already found an error
      return;
    }
    // set error on matchingControl if validation fails
    if (!isBeforeOrEqual(startControl.value, endControl.value)) {
      startControl.setErrors({ invalidDate: true });
    } else {
      startControl.setErrors(null);
    }
  };
}

export function isBeforeOrEqual(startDate: string, endDate: string): boolean {
  return new Date(endDate) >= new Date(startDate);
}
