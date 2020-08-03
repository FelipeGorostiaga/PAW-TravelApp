import {Component, Input, OnInit} from '@angular/core';
import {Activity} from "../../model/activity";
import {User} from "../../model/user";
import {ModalService} from "../../modal";

@Component({
  selector: 'app-activities',
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.scss']
})
export class ActivitiesComponent implements OnInit {

  @Input() activities: Activity[];
  @Input() isAdmin: boolean;
  @Input() loggedUser: User;

  isEmpty: boolean;

  constructor(private modalService: ModalService) { }

  ngOnInit() {
    if (!this.activities || this.activities.length === 0) {
      this.isEmpty = true;
    }
  }

  createActivity() {

  }

  openModal(id: string) {
    this.modalService.open(id);
  }

  closeModal(id: string) {
    this.modalService.close(id);
  }


}
