import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Trip} from '../model/trip';
import {UserAuth} from '../model/user-auth';
import {User} from '../model/user';
import {UserForm} from '../model/user-form';



@Injectable({
  providedIn: 'root'
})
export class ApiUserService {

    private BASE_URL = 'http://localhost:8080/api';
    private USERS_BASE_URL = `${this.BASE_URL}/users/`;
    public CREATE_USER_URL = `${this.BASE_URL}/create`;
    public AUTHENTICATE_URL = `${this.BASE_URL}/users/authenticate`;

    constructor(private http: HttpClient) {}

    // ------------------------ Users ---------------------

    getUserTrips(id: string, page: number): Observable<Trip[]> {
        const url = this.USERS_BASE_URL + id  + '/trips';
        return this.http.get<Trip[]>(url);
    }

    authenticateUser(userAuth: UserAuth): Observable<any>  {
        return this.http.post(this.AUTHENTICATE_URL, userAuth);
    }


    getUserById(id: string): Observable<User> {
        const url = this.BASE_URL + '/users/' + id;
        return this.http.get<User>(url);
    }

    createUser(userForm: UserForm): Observable<User> {
        return this.http.post<User>(this.CREATE_USER_URL, userForm);
    }

    getUserPicture(id: string): Observable<any> {
        const url = this.BASE_URL + '/users/' + id + '/picture';
        return this.http.get(url);
    }

    // ------------------------------------------------


}
