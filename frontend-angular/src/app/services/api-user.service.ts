import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Trip} from '../model/trip';
import {UserAuth} from '../model/user-auth';
import {User} from '../model/user';
import {UserForm} from '../model/forms/user-form';
import {TripForm} from '../model/forms/trip-form';
import {ImageDTO} from '../model/image-dto';
import {Place} from '../model/place';
import {TripComment} from '../model/trip-comment';
import {Activity} from '../model/activity';
import {ActivityForm} from '../model/forms/activity-form';


@Injectable({
  providedIn: 'root'
})
export class ApiUserService {

    private baseURL = 'http://localhost:8080/api';
    private usersBaseURL = `${this.baseURL}/users/`;
    private tripsBaseURL = `${this.baseURL}/trips/`;

    constructor(private http: HttpClient) {}

    // ------------------------ Users ---------------------

    getUserTrips(id: string, page: number): Observable<Trip[]> {
        const url = this.usersBaseURL + id  + '/trips';
        return this.http.get<Trip[]>(url);
    }

    authenticateUser(userAuth: UserAuth): Observable<any>  {
        const url = this.usersBaseURL + '/authenticate';
        return this.http.post(url, userAuth);
    }


    getUserById(id: string): Observable<User> {
        const url = this.usersBaseURL + id;
        return this.http.get<User>(url);
    }

    createUser(userForm: UserForm): Observable<User> {
        const url = this.usersBaseURL + '/create';
        return this.http.post<User>(url, userForm);
    }

    getUserPicture(id: string): Observable<ImageDTO> {
        const url = this.usersBaseURL + id + '/picture';
        return this.http.get(url);
    }

    // ----------------------- Trips -------------------------

    getAlltrips(pageNum: number): Observable<Trip[]> {
        const params = new HttpParams().set('page', String(pageNum));
        return this.http.get<Trip[]>(this.tripsBaseURL, {params});
    }

    editTrip(id: string, tripForm: TripForm): Observable<Trip> {
        const url = this.tripsBaseURL + id + '/edit';
        return this.http.put<Trip>(url, tripForm);
    }

    createTrip(tripForm: TripForm): Observable<Trip> {
        const url = this.tripsBaseURL + '/create';
        return this.http.post(url, tripForm);
    }

    getTrip(id: string): Observable<Trip> {
        const url = this.tripsBaseURL + id;
        return this.http.get<Trip>(url);
    }

    getTripPlaces(id: string): Observable<Place[]> {
        const url = this.tripsBaseURL + id + '/places';
        return this.http.get<Place[]>(url);
    }

    deleteTrip(id: string): Observable<any> {
        const url = this.tripsBaseURL + id + '/delete';
        return this.http.delete(url);
    }

    // Todo: fix sending User instead of userId and fix API receive
    addUserToTrip(userId: string, tripId: string): Observable<any> {
        const url = this.tripsBaseURL + tripId + '/add/' + userId;
        return this.http.put(url, {});
    }

    removeUserFromTrip(userId: string, tripId: string): Observable<any> {
        const url = this.tripsBaseURL + tripId + '/remove/' + userId;
        return this.http.put(url, {});
    }

    getTripImage(id: string): Observable<ImageDTO> {
        const url = this.tripsBaseURL + id + '/image';
        return this.http.get<ImageDTO>(url);
    }

    getTripComments(id: string): Observable<TripComment>  {
        const url = this.tripsBaseURL + id + '/comments';
        return this.http.get<TripComment>(url);
    }

    // Todo: TripCommentForm? and fix SENDING USER ID in API
    postComment(tripId: string, userId: string, comment: TripComment): Observable<TripComment> {
        const url = this.tripsBaseURL + tripId + '/comments/add/' + userId;
        return this.http.post<TripComment>(url, comment);
    }

    getTripActivities(id: string): Observable<Activity[]> {
        const url = this.tripsBaseURL + id + '/activities';
        return this.http.get<Activity[]>(url);
    }

    createTripActivity(id: string, activityForm: ActivityForm) {
        const url = this.tripsBaseURL + id + '/activities/create';
    }

    deleteTripActivity(tripId: string, activityId: string): Observable<any> {
        const url = this.tripsBaseURL + tripId + '/activities/delete/' + activityId;
        return this.http.delete(url);
    }
}
