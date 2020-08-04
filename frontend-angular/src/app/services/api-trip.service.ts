import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Trip} from "../model/trip";
import {TripForm} from "../model/forms/trip-form";
import {Place} from "../model/place";
import {ImageDTO} from "../model/image-dto";
import {Activity} from "../model/activity";
import {ActivityForm} from "../model/forms/activity-form";
import {HttpClient} from "@angular/common/http";
import {User} from "../model/user";
import {Comment} from "../model/comment";

@Injectable({
  providedIn: 'root'
})
export class ApiTripService {

  constructor(private http: HttpClient) {}

  private baseURL = 'http://localhost:8080/api';
  private tripsBaseURL = `${this.baseURL}/trips`;

  getTripUsersAmount(id: number): Observable<any> {
    const url = this.tripsBaseURL + '/' + id + '/users/amount';
    return this.http.get(url);
  }

  getAllTripsPerPage(pageNum: number): Observable<any> {
    const url = this.tripsBaseURL + '/all' + pageNum;
    // const params = new HttpParams().set('page', String(pageNum));
    return this.http.get(this.tripsBaseURL);
  }

  getAllTrips(): Observable<any> {
    const url = this.tripsBaseURL + '/all';
    return this.http.get(url);
  }

  editTrip(id: number, tripForm: TripForm): Observable<Trip> {
    const url = this.tripsBaseURL + '/' + id + '/edit';
    return this.http.put<Trip>(url, tripForm);
  }

  createTrip(tripForm: TripForm): Observable<Trip> {
    const url = this.tripsBaseURL + '/create';
    return this.http.post<Trip>(url, tripForm);
  }

  getTrip(id: number): Observable<Trip> {
    const url = this.tripsBaseURL + '/' + id;
    return this.http.get<Trip>(url);
  }

  getTripPlaces(id: number): Observable<any> {
    const url = this.tripsBaseURL + '/' + id + '/places';
    return this.http.get(url);
  }

  deleteTrip(id: number): Observable<any> {
    const url = this.tripsBaseURL + '/' + id + '/delete';
    return this.http.delete(url);
  }

  // Todo: fix sending User instead of userId and fix API receive
  addUserToTrip(userId: number, tripId: number): Observable<any> {
    const url = this.tripsBaseURL + '/' + tripId + '/add/' + userId;
    return this.http.put(url, {});
  }

  removeUserFromTrip(userId: number, tripId: number): Observable<any> {
    const url = this.tripsBaseURL + '/' + tripId + '/remove/' + userId;
    return this.http.put(url, {});
  }

  getTripImage(id: number): Observable<ImageDTO> {
    const url = this.tripsBaseURL + '/' + id + '/image';
    return this.http.get<ImageDTO>(url);
  }

  getTripComments(id: number): Observable<any>  {
    const url = this.tripsBaseURL + '/' + id + '/comments';
    return this.http.get(url);
  }

  // Todo: TripCommentForm? and fix SENDING USER ID in API
  postComment(tripId: number, userId: number, comment: Comment): Observable<Comment> {
    const url = this.tripsBaseURL + '/' + tripId + '/comments/add/' + userId;
    return this.http.post<Comment>(url, comment);
  }

  getTripActivities(id: number): Observable<any> {
    const url = this.tripsBaseURL + '/' + id + '/activities';
    return this.http.get(url);
  }

  createTripActivity(id: number, activityForm: ActivityForm): Observable<any> {
    const url = this.tripsBaseURL + '/' + id + '/activities/create';
    return this.http.post(url, activityForm);
  }

  deleteTripActivity(tripId: number, activityId: number): Observable<any> {
    const url = this.tripsBaseURL + '/' + tripId + '/activities/delete/' + activityId;
    return this.http.delete(url);
  }

  getTripAdmins(tripId: number): Observable<any> {
    const url = this.tripsBaseURL + '/' + tripId + '/admins';
    return this.http.get(url);
  }

  getTripUsers(tripId: number): Observable<any> {
    const url = this.tripsBaseURL + '/' + tripId + '/users';
    return this.http.get(url);
  }
}
