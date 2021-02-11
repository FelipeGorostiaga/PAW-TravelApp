import {Component, Input, OnInit} from '@angular/core';
import {Rate} from "../../model/rate";

@Component({
  selector: 'app-rate-tile',
  templateUrl: './rate-tile.component.html',
  styleUrls: ['./rate-tile.component.scss']
})
export class RateTileComponent implements OnInit {

  @Input() rate: Rate;

  constructor() { }

  ngOnInit(): void {
  }

}
