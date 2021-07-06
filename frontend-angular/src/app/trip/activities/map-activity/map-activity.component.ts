import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Activity} from "../../../model/activity";
import {ApiTripService} from "../../../services/api-trip.service";
import {DateUtilService} from "../../../services/date-util.service";

@Component({
    selector: 'app-map-activity',
    templateUrl: './map-activity.component.html',
    styleUrls: ['./map-activity.component.scss']
})
export class MapActivityComponent implements OnInit {

    @Input() activity: Activity;
    @Input() isAdmin: boolean;
    @Input() completed: boolean;
    @Output()
    deleteActivityEvent: EventEmitter<number> = new EventEmitter<number>();
    zoom: number;
    showMap: boolean;

    userLang: string;

    popoverTitle: string;
    popoverMessage: string;

    startDate: Date;
    endDate: Date;

    constructor(private tripService: ApiTripService,
                private dateUtils: DateUtilService) {
    }

    ngOnInit(): void {
        // @ts-ignore
        this.userLang = (navigator.language || navigator.userLanguage).substr(0, 2);
        if (this.userLang === 'es') {
            this.popoverTitle = "Eliminar actividad";
            this.popoverMessage = "Estas seguro que deseas eliminar esta actividad?";
        } else {
            this.popoverTitle = "Delete activity";
            this.popoverMessage = "Are you sure you want to delete this activity?";
        }
        this.startDate = this.dateUtils.stringToDate(this.activity.startDate);
        this.endDate = this.dateUtils.stringToDate(this.activity.endDate);
        this.zoom = 14;
    }

    toggle() {
        this.showMap = !this.showMap;
    }

    deleteActivity() {
        // @ts-ignore
        this.deleteActivityEvent.emit(this.activity);
    }
}
