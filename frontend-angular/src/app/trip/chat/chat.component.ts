import {Component, Input, OnInit} from '@angular/core';
import {FullTrip, Trip} from "../../model/trip";
import {ApiTripService} from "../../services/api-trip.service";
import {AuthService} from "../../services/auth/auth.service";
import {CommentForm} from "../../model/forms/comment-form";


@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

    @Input() trip: FullTrip;
    @Input() isAdmin: boolean;
    @Input() completed: boolean;

    sendingMessage: boolean;

    constructor(private tripService: ApiTripService, private authService: AuthService) {
    }

    ngOnInit() {
        this.sendingMessage = false;
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
                this.trip.comments.push(data);
                this.sendingMessage = false;
            }
        );
    }
}
