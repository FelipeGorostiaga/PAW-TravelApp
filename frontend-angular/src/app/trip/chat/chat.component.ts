import {Component, Input, OnInit} from '@angular/core';
import {Trip} from "../../model/trip";
import {ApiTripService} from "../../services/api-trip.service";
import {AuthService} from "../../services/auth/auth.service";
import {CommentForm} from "../../model/forms/comment-form";
import {User} from "../../model/user";


@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

    @Input() trip: Trip;
    @Input() isAdmin: boolean;
    @Input() completed: boolean;
    loggedUser: User;

    sendingMessage: boolean;

    constructor(private tripService: ApiTripService, private authService: AuthService) {
    }

    ngOnInit() {
        this.sendingMessage = false;
        this.loggedUser = this.authService.getLoggedUser();
        if (!this.trip.comments) {
            this.tripService.getTripComments(this.trip.commentsURL).subscribe(
                data => {
                    this.trip.comments = data;
                    this.trip.comments.sort(function(a, b) {
                        // @ts-ignore
                        return new Date(b.createdOn) - new Date(a.createdOn);
                    });
                }
            );
        }
    }

    sendMessage(input) {
        let inputValue = input.value;
        if (inputValue.length === 0 || this.sendingMessage) {
            return;
        }
        this.sendingMessage = true;
        input.value = "";
        this.tripService.postComment(this.trip.id, new CommentForm(String(inputValue))).subscribe(
            data => {
                this.trip.comments.splice(0, 0, data);
                this.sendingMessage = false;
            }
        );
    }
}
