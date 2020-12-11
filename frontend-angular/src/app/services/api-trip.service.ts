import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Trip} from "../model/trip";
import {TripForm} from "../model/forms/trip-form";
import {ImageDTO} from "../model/image-dto";
import {ActivityForm} from "../model/forms/activity-form";
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {CommentForm} from "../model/forms/comment-form";
import {shareReplay} from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class ApiTripService {

    constructor(private http: HttpClient) {
    }

    private tripsBaseURL = `${environment.apiURL}/trips/`;

    getTripUsersAmount(id: number): Observable<any> {
        const url = this.tripsBaseURL + id + '/users/amount';
        return this.http.get(url);
    }

    getAllTripsPerPage(pageNum: number): Observable<any> {
        const url = this.tripsBaseURL + 'all' + pageNum;
        // const params = new HttpParams().set('page', String(pageNum));
        return this.http.get(this.tripsBaseURL);
    }

    getAllTrips(): Observable<any> {
        const url = this.tripsBaseURL + 'all';
        return this.http.get(url);
    }

    editTrip(id: number, tripForm: TripForm): Observable<Trip> {
        const url = this.tripsBaseURL + id + '/edit';
        return this.http.put<Trip>(url, tripForm);
    }

    createTrip(tripForm: TripForm): Observable<Trip> {
        const url = this.tripsBaseURL + 'create';
        return this.http.post<Trip>(url, tripForm);
    }

    getTrip(id: number): Observable<any> {
        const url = this.tripsBaseURL + id;
        return this.http.get<Trip>(url);
    }

    getTripPlaces(id: number): Observable<any> {
        const url = this.tripsBaseURL + id + '/places';
        return this.http.get(url);
    }

    deleteTrip(id: number): Observable<any> {
        const url = this.tripsBaseURL + id + '/delete';
        return this.http.delete(url);
    }

    addUserToTrip(userId: number, id: number): Observable<any> {
        const url = this.tripsBaseURL + id + '/add/' + userId;
        return this.http.put(url, {});
    }

    removeUserFromTrip(userId: number, id: number): Observable<any> {
        const url = this.tripsBaseURL +  id + '/remove/' + userId;
        return this.http.put(url, {});
    }

    getTripImage(id: number): Observable<ImageDTO> {
        const url = this.tripsBaseURL + id + '/image';
        return this.http.get<ImageDTO>(url);
    }


    postComment(id: number, form: CommentForm): Observable<any> {
        const url = this.tripsBaseURL + id + '/comments/add';
        return this.http.post(url, form).pipe(shareReplay());
    }


    createTripActivity(id: number, activityForm: ActivityForm): Observable<any> {
        const url = this.tripsBaseURL + id + '/activities/create';
        return this.http.post(url, activityForm);
    }

    deleteTripActivity(id: number, activityId: number): Observable<any> {
        const url = this.tripsBaseURL + id + '/activities/delete/' + activityId;
        return this.http.delete(url);
    }

    sendJoinRequest(id: number, userId: number) {
        const url = this.tripsBaseURL + id + '/request/invite';
        return this.http.post(url, {userId: userId, tripId: id});
    }

    respondJoinRequest(id: number, token: string, accepted: boolean) {
        const url = this.tripsBaseURL + id + "/invitation/pending";
        let params = new HttpParams().set("token", token).set("accepted", String(accepted));
        return this.http.post(url, {},{params: params});
    }
}
