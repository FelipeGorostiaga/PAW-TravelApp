import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Trip} from '../model/trip';
import {environment} from "../../environments/environment";
import {UserTripsData} from "../model/UserTripsData";
import {User} from "../model/user";
import {Rate} from "../model/rate";


@Injectable({
    providedIn: 'root'
})
export class ApiUserService {

    constructor(private http: HttpClient) {
    }

    private usersBaseURL = `${environment.apiURL}/users`;

    getUser(id: number): Observable<User> {
        return this.http.get<User>(`${this.usersBaseURL}/${id}`);
    }

    getUserTrips(id: number, page: number): Observable<any> {
        const params = new HttpParams().set('page', String(page));
        return this.http.get<Trip[]>(`${this.usersBaseURL}/${id}/trips`, {params: params});
    }

    getUserPicture(url: string): Observable<any> {
        return this.http.get(url, {responseType: 'blob'});
    }

    editProfile(formData: FormData, id: number): Observable<any> {
        return this.http.put(`${this.usersBaseURL}/${id}`, formData);
    }

    getUserTripsData(url: string): Observable<UserTripsData> {
        return this.http.get<UserTripsData>(url);
    }

    // rates the user received
    getUserRates(url: string): Observable<Rate[]>  {
        return this.http.get<Rate[]>(url);
    }

    // rates the user needs to write
    getUserPendingRates(url: string): Observable<any> {
        return this.http.get(url);
    }

    getUserRating(id: number): Observable<number> {
        return this.http.get<number>(`${this.usersBaseURL}/${id}/rating`);
    }

    rateUser(rateForm: any) {
        return this.http.post(`${this.usersBaseURL}/rates`, rateForm);
    }

    getUserInvitations(url: string): Observable<any> {
        return this.http.get(url);
    }
}
