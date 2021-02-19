import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {FullTrip, Trip} from "../model/trip";
import {TripForm} from "../model/forms/trip-form";
import {ActivityForm} from "../model/forms/activity-form";
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {CommentForm} from "../model/forms/comment-form";
import {mergeMap, shareReplay} from "rxjs/operators";
import {User} from "../model/user";

@Injectable({
    providedIn: 'root'
})
export class ApiTripService {

    constructor(private http: HttpClient) {
    }

    private tripsBaseURL = `${environment.apiURL}/trips`;

    getTrip(id: number): Observable<any> {
        const url = this.tripsBaseURL + id;
        return this.http.get<Trip>(url);
    }

    getTripsForPage(page: number): Observable<any> {
        let params = new HttpParams().set("page", String(page));
        return this.http.get(this.tripsBaseURL, {params: params});
    }

    editTrip(formData: FormData, tripId: number): Observable<any> {
        const url = this.tripsBaseURL + tripId + '/edit';
        return this.http.post(url, formData);
    }

    createTrip(tripForm: TripForm): Observable<Trip> {
        const url = this.tripsBaseURL;
        return this.http.post<Trip>(url, tripForm);
    }

    getTripPlaces(id: number): Observable<any> {
        const url = this.tripsBaseURL + id + '/places';
        return this.http.get(url);
    }

    deleteTrip(id: number): Observable<any> {
        const url = this.tripsBaseURL + id;
        return this.http.delete(url);
    }

    exitTrip(tripId: number): Observable<any> {
        const url = this.tripsBaseURL + tripId + '/exit';
        return this.http.post(url, {});
    }

    inviteUserToTrip(id: number, userId: number): Observable<any> {
        const url = this.tripsBaseURL + id + '/invite/' + userId;
        return this.http.post(url, {});
    }

    getTripImage(id: number): Observable<any> {
        const url = this.tripsBaseURL + id + '/image';
        return this.http.get(url, {responseType: 'blob'});
    }

    getTripCardImage(tripId: number) {
        const url = this.tripsBaseURL + tripId + '/image/card';
        return this.http.get(url, {responseType: 'blob'});
    }

    postComment(id: number, form: CommentForm): Observable<any> {
        const url = this.tripsBaseURL + id + '/comments';
        return this.http.post(url, form).pipe(shareReplay());
    }

    createTripActivity(id: number, activityForm: ActivityForm): Observable<any> {
        const url = this.tripsBaseURL + id + '/activities';
        return this.http.post(url, activityForm);
    }

    deleteTripActivity(id: number, activityId: number): Observable<any> {
        const url = this.tripsBaseURL + id + '/activities/' + activityId;
        return this.http.delete(url);
    }

    sendJoinRequest(id: number, userId: number): Observable<any> {
        const url = this.tripsBaseURL + id + '/request-invite';
        return this.http.post(url, {});
    }

    respondJoinRequest(id: number, token: string, accepted: boolean): Observable<any> {
        const url = this.tripsBaseURL + id + "/invitation";
        let params = new HttpParams().set("token", token).set("accepted", String(accepted));
        return this.http.post(url, {},{params: params});
    }

    isWaitingTripConfirmation(id: number, userId: number) : Observable<any> {
        const url = this.tripsBaseURL + id + "/pendingConfirmations/user";
        let params = new HttpParams().set("user", String(userId));
        return this.http.get(url, {params: params})
    }

    respondTripInvite(id: number, token: string, accepted: boolean): Observable<any> {
        const url = this.tripsBaseURL + id + "/invite-request/response";
        let params = new HttpParams().set("token", token).set("accepted", String(accepted));
        return this.http.post(url, {}, {params: params}).pipe(mergeMap(res => this.getTrip(id)));
    }

    getTripPendingConfirmations(id: number, userId: number): Observable<any> {
        const url = this.tripsBaseURL + id + "/pendingConfirmations";
        return this.http.get(url);
    }

    grantAdminRole(trip: FullTrip, user: User) {
        const url = this.tripsBaseURL + trip.id + '/make-admin/' + user.id;
        return this.http.post(url, {});
    }

    finishTrip(tripId: number) {
        const url = this.tripsBaseURL + tripId + '/finish';
        return this.http.post(url, {});
    }


}
