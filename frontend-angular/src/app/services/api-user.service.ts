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

    getUserProfileData(userId: number): Observable<UserProfile> {
        const url = this.usersBaseURL + userId + '/profile';
        return this.http.get<UserProfile>(url);
    }

    // rates the user received
    getUserRates(userId: number): Observable<any>  {
        const url = this.usersBaseURL + userId + '/rates';
        return this.http.get(url);
    }

    // rates the user need to write
    getUserPendingRates(userId: number): Observable<any> {
        const url = this.usersBaseURL + userId + '/pending/rates';
        return this.http.get(url);
    }

    getUserRating(userId: number): Observable<number> {
        const url = this.usersBaseURL + userId + '/rating'
        return this.http.get<number>(url);
    }

    submitRate(data: any) {
        const url = this.usersBaseURL + 'rateUser';
        return this.http.post(url, data);
    }
}
