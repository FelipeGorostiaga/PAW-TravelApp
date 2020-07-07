import { Injectable } from '@angular/core';
import {UserForm} from '../../model/forms/user-form';
import {HttpClient} from '@angular/common/http';
import {UserAuth} from '../../model/user-auth';
import {Observable} from 'rxjs';
import {User} from '../../model/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

    private baseURL = 'http://localhost:8080/api';
    private usersBaseURL = `${this.baseURL}/users/`;

    constructor(private http: HttpClient) { }


    isLoggedIn() {
        return !!localStorage.getItem('token');
    }

    getJwtToken() {
        return localStorage.getItem('token');
    }

    setJwtToken(jwt: string) {
        localStorage.setItem('token', jwt);
    }

    register(userForm: UserForm): Observable<User> {
        const url = this.usersBaseURL + '/create';
        return this.http.post<User>(url, userForm);
    }


    login(userAuth: UserAuth): Observable<any>  {
        const url = this.usersBaseURL + '/authenticate';
        return this.http.post(url, userAuth);
    }

    logout() {
        localStorage.removeItem('token');
    }
}
