import {Component, Input, OnInit} from '@angular/core';
import {Rate} from "../../../model/rate";
import {DateUtilService} from "../../../services/date-util.service";

@Component({
  selector: 'app-rate-tile',
  templateUrl: './rate-tile.component.html',
  styleUrls: ['./rate-tile.component.scss']
})
export class RateTileComponent implements OnInit {

  @Input() rate: Rate;

  createdOn: Date;

  constructor(private dateUtil: DateUtilService) { }

  ngOnInit(): void {
    this.createdOn = this.dateUtil.stringToDate(this.rate.createdOn);
  }

}
