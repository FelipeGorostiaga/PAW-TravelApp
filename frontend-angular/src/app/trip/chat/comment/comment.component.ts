import {Component, Input, OnInit} from '@angular/core';
import {Comment} from "../../../model/comment";
import {AuthService} from "../../../services/auth/auth.service";
import {User} from "../../../model/user";


@Component({
    selector: 'app-comment',
    templateUrl: './comment.component.html',
    styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {

    createdOn: Date;

    @Input() comment: Comment;
    loggedUser: User;

    constructor(private authService: AuthService) {
        this.loggedUser = authService.getLoggedUser();
    }

    ngOnInit() {
        this.createdOn = new Date(this.comment.createdOn);
    }

}
