import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-rate-form',
  templateUrl: './rate-form.component.html',
  styleUrls: ['./rate-form.component.scss']
})
export class RateFormComponent implements OnInit {

  rating: number = 0;
  comment: string = "";

  constructor() { }

  ngOnInit(): void {
  }

}
