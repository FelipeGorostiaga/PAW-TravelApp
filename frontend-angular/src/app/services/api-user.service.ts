import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Trip} from '../model/trip';
import {environment} from "../../environments/environment";


@Injectable({
    providedIn: 'root'
})
export class ApiUserService {

    constructor(private http: HttpClient) {
    }

    private usersBaseURL = `${environment.apiURL}/users/`;

    getUserById(id: number): Observable<any> {
        const url = this.usersBaseURL + id;
        return this.http.get(url);
    }

    getUserTrips(userId: number): Observable<Trip[]> {
        const url = this.usersBaseURL + userId + '/trips';
        return this.http.get<Trip[]>(url);
    }

    getUserPicture(id: number): Observable<any> {
        const url = this.usersBaseURL + id + '/picture';
        return this.http.get(url, {responseType: 'blob'});
    }

    editBiography(formData: FormData, userId: number): Observable<any> {
        const url = this.usersBaseURL + userId + '/edit/biography';
        return this.http.post(url, {"biography": formData.get('biography')});
    }

    editProfilePicture(formData: FormData, userId: number) {
        const url = this.usersBaseURL + userId + '/edit/picture';
        return this.http.post(url, formData);
    }

}
