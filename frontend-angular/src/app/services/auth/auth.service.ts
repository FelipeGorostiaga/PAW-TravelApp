import {Injectable} from '@angular/core';
import {UserForm} from '../../model/forms/user-form';
import {HttpClient, HttpParams} from '@angular/common/http';
import {UserAuth} from '../../model/user-auth';
import {Observable, throwError} from 'rxjs';
import {User} from '../../model/user';
import {Router} from "@angular/router";
import {environment} from "../../../environments/environment";
import {catchError, tap} from "rxjs/operators";
import {RefreshTokenResponse} from "../../model/RefreshTokenResponse";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private usersBaseURL = `${environment.apiURL}/users`;

    private authBaseURL = `${environment.apiURL}/authenticate`;

    constructor(private http: HttpClient, private router: Router) {
    }

    register(userForm: UserForm): Observable<any> {
        return this.http.post<User>(this.usersBaseURL, userForm).pipe(
            catchError(err => {
                return throwError(err.error);
            })
        );
    }

    login(userAuth: UserAuth): Observable<any> {
        return this.http.post<string>(this.authBaseURL, userAuth);
    }

    verifyAccount(verificationCode: string): Observable<any> {
        const url = this.authBaseURL + '/verify';
        let params = new HttpParams().set("code", verificationCode);
        return this.http.get(url, {params: params});
    }

    refreshToken() {
        return this.http.get(`${this.authBaseURL}/refresh`, {
            headers: {
                'x-refresh-token': this.getRefreshToken()
            }
        }).pipe(
            tap((data: RefreshTokenResponse) => {
                this.setAccessToken(data.accessToken);
            })
        );
    }

    isLoggedIn() {
        return !!localStorage.getItem('accessToken');
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

}
