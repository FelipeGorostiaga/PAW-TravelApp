import {Injectable} from '@angular/core';
import {UserForm} from '../../model/forms/user-form';
import {HttpClient, HttpParams} from '@angular/common/http';
import {UserAuth} from '../../model/user-auth';
import {Observable} from 'rxjs';
import {User} from '../../model/user';
import {Router} from "@angular/router";
import {environment} from "../../../environments/environment";
import {tap} from "rxjs/operators";
import {RefreshTokenResponse} from "../../model/RefreshTokenResponse";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

    private usersBaseURL = `${environment.apiURL}/users`;

    constructor(private http: HttpClient, private router: Router) { }

    isLoggedIn() {
        return !!localStorage.getItem('accessToken');
    }

    register(userForm: UserForm): Observable<any> {
        const url = this.usersBaseURL + '/create';
        return this.http.post<User>(url, userForm);
    }

    login(userAuth: UserAuth): Observable<any> {
        const url = this.usersBaseURL + '/authenticate';
        return this.http.post<string>(url, userAuth);
    }

    refreshToken() {
        return this.http.get(`${this.usersBaseURL}/refresh`, {
            headers: {
                'x-refresh-token': this.getRefreshToken()
            }
        }).pipe(
            tap((data: RefreshTokenResponse) => {
                console.log("JWT token refreshed")
                this.setAccessToken(data.accessToken);
            })
        );
    }

    logout() {
        this.removeSession();
        this.router.navigate(["/login"]);
    }

    removeSession() {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('loggedUser');
    }

    createSession(accessToken: string, refreshToken: string, user: any) {
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('loggedUser', JSON.stringify(user));
    }

    getLoggedUser(): User {
        return JSON.parse(localStorage.getItem('loggedUser'));
    }

    getAccessToken(): string {
        return localStorage.getItem('accessToken');
    }

    setAccessToken(accessToken: string) {
        localStorage.setItem('accessToken', accessToken);
    }

    getRefreshToken(): string {
        return localStorage.getItem('refreshToken');
    }

    setRefreshToken(refreshToken: string) {
        localStorage.setItem('refreshToken', refreshToken);
    }

    verifyAccount(verificationCode: string) {
        const url = this.usersBaseURL + '/verify';
        let params = new HttpParams().set("code", verificationCode);
        return this.http.get(url, {params: params});
    }
}
