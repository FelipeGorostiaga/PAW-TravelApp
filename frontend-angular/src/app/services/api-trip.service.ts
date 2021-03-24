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
import {TripInvitation} from "../model/forms/TripInvitation";
import {InviteRequest} from "../model/InviteRequest";

@Injectable({
    providedIn: 'root'
})
export class ApiTripService {

    constructor(private http: HttpClient) {
    }

    private tripsBaseURL = `${environment.apiURL}/trips`;

    getTrip(id: number): Observable<any> {
        return this.http.get<Trip>(`${this.tripsBaseURL}/${id}`);
    }


    createTrip(tripForm: TripForm): Observable<Trip> {
        return this.http.post<Trip>(this.tripsBaseURL, tripForm);
    }

    editTrip(formData: FormData, tripId: number): Observable<any> {
        const url = this.tripsBaseURL + "/" + tripId + '/edit';
        return this.http.post(url, formData);
    }

    getTripsForPage(page: number): Observable<any> {
        let params = new HttpParams().set("page", String(page));
        return this.http.get(this.tripsBaseURL, {params: params});
    }

    getTripPlaces(id: number): Observable<any> {
        return this.http.get(`${this.tripsBaseURL}/${id}/places`);
    }

    deleteTrip(id: number): Observable<any> {
        return this.http.delete(`${this.tripsBaseURL}/${id}`);
    }

    exitTrip(id: number): Observable<any> {
        return this.http.put(`${this.tripsBaseURL}/${id}/exit`, {});
    }

    getTripImage(id: number): Observable<any> {
        return this.http.get(`${this.tripsBaseURL}/${id}/image`, {responseType: 'blob'});
    }

    getTripCardImage(id: number) {
        return this.http.get(`${this.tripsBaseURL}/${id}/image-card`, {responseType: 'blob'});
    }

    postComment(id: number, form: CommentForm): Observable<any> {
        return this.http.post(`${this.tripsBaseURL}/${id}/comments`, form).pipe(shareReplay());
    }

    createTripActivity(id: number, activityForm: ActivityForm): Observable<any> {
        return this.http.post(`${this.tripsBaseURL}/${id}/activities`, activityForm);
    }

    deleteTripActivity(id: number, activityId: number): Observable<any> {
        return this.http.delete(`${this.tripsBaseURL}/${id}/activities/${activityId}`);
    }

    inviteUserToTrip(id: number, invitationForm: TripInvitation): Observable<any> {
        return this.http.post(`${this.tripsBaseURL}/${id}/invitation`, invitationForm);
    }

    respondTripInvite(id: number, token: string, accepted: boolean): Observable<any> {
        let params = new HttpParams().set("token", token).set("accepted", String(accepted));
        return this.http.put(`${this.tripsBaseURL}/${id}/invitation`, {}, {params: params})
            .pipe(mergeMap(res => this.getTrip(id)));
    }

    sendJoinRequest(id: number, userId: number): Observable<any> {
        return this.http.post(`${this.tripsBaseURL}/${id}/inviteRequests`, {});
    }

    respondJoinRequest(id: number, token: string, accepted: boolean): Observable<any> {
        let params = new HttpParams().set("token", token).set("accepted", String(accepted));
        return this.http.post(`${this.tripsBaseURL}/${id}/inviteRequests`, {}, {params: params});
    }

    getPendingConfirmations(id: number, userId: number): Observable<InviteRequest[]> {
        return this.http.get<InviteRequest[]>(`${this.tripsBaseURL}/${id}/inviteRequests`);
    }

    grantAdminRole(id: number, userId: number) {
        return this.http.post(`${this.tripsBaseURL}/${id}/admins/${userId}`, {});
    }

    finishTrip(id: number) {
        return this.http.post(`${this.tripsBaseURL}/${id}/finish`, {});
    }

    hasImage(id: number) {
        return this.http.get(`${this.tripsBaseURL}/${id}/hasImage/${id}`);
    }
}
