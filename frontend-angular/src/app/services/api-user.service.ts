import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Trip} from '../model/trip';
import {ImageDTO} from '../model/image-dto';
import {environment} from "../../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class ApiUserService {

    constructor(private http: HttpClient) {}

    private usersBaseURL = `${environment.apiURL}/users/`;

    getUserById(id: number): Observable<any> {
        const url = this.usersBaseURL + id;
        return this.http.get(url);
    }

    getUserTrips(userId: number): Observable<Trip[]> {
        const url = this.usersBaseURL + userId  + '/trips';
        return this.http.get<Trip[]>(url);
    }

    getUserPicture(id: number): Observable<ImageDTO> {
        const url = this.usersBaseURL + id + '/picture';
        return this.http.get<ImageDTO>(url);
    }

}
