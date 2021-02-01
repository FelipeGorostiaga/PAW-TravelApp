import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Trip} from '../model/trip';
import {environment} from "../../environments/environment";
import {UserProfile} from "../model/UserProfile";


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

    editProfile(formData: FormData, userId: number): Observable<any> {
        const url = this.usersBaseURL + userId + '/editProfile';
        return this.http.post(url, formData);
    }

    getUserActiveTrips(userId: number): Observable<any> {
        const url = this.usersBaseURL + userId + "/trips/active";
        return this.http.get(url);
    }

    getUserCompletedTrips(userId: number) {
        const url = this.usersBaseURL + userId + "/trips/completed";
        return this.http.get(url);
    }

    getUserDueTrips(userId: number) {
        const url = this.usersBaseURL + userId + "/trips/due";
        return this.http.get(url);
    }

    getUserProfileData(userId: number): Observable<UserProfile> {
        const url = this.usersBaseURL + userId + '/profile';
        return this.http.get<UserProfile>(url);
    }
}
