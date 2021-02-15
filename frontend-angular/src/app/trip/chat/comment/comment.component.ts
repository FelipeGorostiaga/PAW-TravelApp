import {Component, Input, OnInit} from '@angular/core';
import {Comment} from "../../../model/comment";
import {DateUtilService} from "../../../services/date-util.service";


@Component({
    selector: 'app-comment',
    templateUrl: './comment.component.html',
    styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {

    createdOn: Date;

    @Input() comment: Comment;

    constructor() {
    }

    ngOnInit() {
        this.createdOn = new Date(this.comment.createdOn);
    }

}
