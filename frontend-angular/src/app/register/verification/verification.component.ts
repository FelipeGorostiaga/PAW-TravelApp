import { Component, OnInit } from '@angular/core';

declare var require: any;

@Component({
  selector: 'app-verification',
  templateUrl: './verification.component.html',
  styleUrls: ['./verification.component.scss']
})
export class VerificationComponent implements OnInit {

  greenTickImg = require('!!file-loader!../../../assets/images/green_check.png').default;

  constructor() { }

  ngOnInit(): void {
  }

}
