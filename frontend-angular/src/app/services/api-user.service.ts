import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
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
        return this.http.get(`${this.usersBaseURL}/${id}`);
    }

    getUserTrips(id: number, page: number): Observable<any> {
        const params = new HttpParams().set('page', String(page));
        return this.http.get<Trip[]>(`${this.usersBaseURL}/${id}/trips`, {params: params});
    }

    getUserPicture(id: number): Observable<any> {
        return this.http.get(`${this.usersBaseURL}/${id}/picture`, {responseType: 'blob'});
    }

    editProfile(formData: FormData, id: number): Observable<any> {
        return this.http.put(`${this.usersBaseURL}/${id}`, formData);
    }

    getUserProfileData(userId: number): Observable<UserProfile> {
        const url = this.usersBaseURL + userId + '/profile';
        return this.http.get<UserProfile>(url);
    }

    // rates the user received
    getUserRates(id: number): Observable<any>  {
        return this.http.get(`${this.usersBaseURL}/${id}/rates`);
    }

    // rates the user needs to write
    getUserPendingRates(id: number): Observable<any> {
        return this.http.get(`${this.usersBaseURL}/${id}/pending-rates`);
    }

    rateUser(rateForm: any) {
        return this.http.post(`${this.usersBaseURL}/rates`, rateForm);
    }

    getUserRating(id: number): Observable<number> {
        return this.http.get<number>(`${this.usersBaseURL}/${id}/rating`);
    }

}
