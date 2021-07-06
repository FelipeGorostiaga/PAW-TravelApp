import {Component, OnInit} from '@angular/core';
import {AuthService} from "../services/auth/auth.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-index',
    templateUrl: './index.component.html',
    styleUrls: ['./index.component.scss']
})
export class IndexComponent implements OnInit {

    constructor(private authService: AuthService,
                private router: Router) {
    }

    ngOnInit(): void {
        if (this.authService.isLoggedIn()) {
            this.router.navigate(['/home']);
        }
    }

}
