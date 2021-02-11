import {Component, OnInit} from '@angular/core';
import {AuthService} from "../services/auth/auth.service";
import {ApiUserService} from "../services/api-user.service";
import {Router} from "@angular/router";
import {User} from "../model/user";
import {ApiSearchService} from "../services/api-search.service";

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {

    isLoggedIn: boolean;
    loggedUser: User;
    searchInput: string;

    constructor(private authService: AuthService,
                private router: Router,
                private searchService: ApiSearchService) {
    }

    ngOnInit() {
        this.isLoggedIn = this.authService.isLoggedIn();
        if (this.isLoggedIn) {
            this.loggedUser = this.authService.getLoggedUser();
        }
    }

    search() {
        if (!this.searchInput || this.containsSpecialCharacters(this.searchInput)) {
            this.searchInput = "";
            return;
        }
        this.redirectTo('search-result');
    }

    logout() {
        this.authService.logout();
    }

    redirectTo(uri:string){
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
            this.router.navigate([uri], {
                queryParams: {name: this.searchInput}}));
    }

    containsSpecialCharacters(input: string): boolean {
        return input.includes('%') || input.includes('_');
    }

}
