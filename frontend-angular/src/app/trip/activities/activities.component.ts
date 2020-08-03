import {Component, ElementRef, Input, NgZone, OnInit, ViewChild} from '@angular/core';
import {Activity} from "../../model/activity";
import {User} from "../../model/user";
import {ModalService} from "../../modal";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MapsAPILoader} from "@agm/core";
import {ActivatedRoute, Router} from "@angular/router";
import {ApiTripService} from "../../services/api-trip.service";

@Component({
  selector: 'app-activities',
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.scss']
})
export class ActivitiesComponent implements OnInit {

  @Input() activities: Activity[];
  @Input() isAdmin: boolean;
  @Input() loggedUser: User;

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

  constructor(private modalService: ModalService, private mapsAPILoader: MapsAPILoader, private  ngZone: NgZone, private router: Router,
              private ts: ApiTripService, private formBuilder: FormBuilder, private route: ActivatedRoute) { }

  ngOnInit() {
    if (!this.activities || this.activities.length === 0) {
      this.isEmpty = true;
    }

    this.activityForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30) ]],
      category: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      placeInput: ['', Validators.required]
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
    console.log("in create activity");
    const values = this.activityForm.value;
    this.submitted = true;
    if (this.activityForm.invalid) {
      return;
    }

    // TODO: api call and receive
  }

  openModal(id: string) {
    this.modalService.open(id);
  }

  closeModal(id: string) {
    this.modalService.close(id);
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
