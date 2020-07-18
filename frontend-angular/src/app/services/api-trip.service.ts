import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Trip} from "../model/trip";
import {TripForm} from "../model/forms/trip-form";
import {Place} from "../model/place";
import {ImageDTO} from "../model/image-dto";
import {TripComment} from "../model/trip-comment";
import {Activity} from "../model/activity";
import {ActivityForm} from "../model/forms/activity-form";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ApiTripService {

  constructor(private http: HttpClient) {}

  private baseURL = 'http://localhost:8080/api';
  private tripsBaseURL = `${this.baseURL}/trips`;

  getTripUsersAmount(id: number): Observable<number> {
    const url = this.tripsBaseURL + '/' + id + '/users/amount';
    return this.http.get<number>(url);
  }

  getAllTripsPerPage(pageNum: number): Observable<Trip[]> {
    const url = this.tripsBaseURL + '/all' + pageNum;
    // const params = new HttpParams().set('page', String(pageNum));
    return this.http.get<Trip[]>(this.tripsBaseURL);
  }

  getAllTrips(): Observable<Trip[][]> {
    const url = this.tripsBaseURL + '/all';
    return this.http.get<Trip[][]>(this.tripsBaseURL);
  }

  editTrip(id: number, tripForm: TripForm): Observable<Trip> {
    const url = this.tripsBaseURL + id + '/edit';
    return this.http.put<Trip>(url, tripForm);
  }

  createTrip(tripForm: TripForm): Observable<Trip> {
    const url = this.tripsBaseURL + '/create';
    return this.http.post<Trip>(url, tripForm);
  }

  getTrip(id: number): Observable<Trip> {
    const url = this.tripsBaseURL + id;
    return this.http.get<Trip>(url);
  }

  getTripPlaces(id: number): Observable<Place[]> {
    const url = this.tripsBaseURL + id + '/places';
    return this.http.get<Place[]>(url);
  }

  deleteTrip(id: number): Observable<any> {
    const url = this.tripsBaseURL + id + '/delete';
    return this.http.delete(url);
  }

  // Todo: fix sending User instead of userId and fix API receive
  addUserToTrip(userId: number, tripId: number): Observable<any> {
    const url = this.tripsBaseURL + tripId + '/add/' + userId;
    return this.http.put(url, {});
  }

  removeUserFromTrip(userId: number, tripId: number): Observable<any> {
    const url = this.tripsBaseURL + tripId + '/remove/' + userId;
    return this.http.put(url, {});
  }

  getTripImage(id: number): Observable<ImageDTO> {
    const url = this.tripsBaseURL + id + '/image';
    return this.http.get<ImageDTO>(url);
  }

  getTripComments(id: number): Observable<TripComment>  {
    const url = this.tripsBaseURL + id + '/comments';
    return this.http.get<TripComment>(url);
  }

  // Todo: TripCommentForm? and fix SENDING USER ID in API
  postComment(tripId: number, userId: number, comment: TripComment): Observable<TripComment> {
    const url = this.tripsBaseURL + tripId + '/comments/add/' + userId;
    return this.http.post<TripComment>(url, comment);
  }

  getTripActivities(id: number): Observable<Activity[]> {
    const url = this.tripsBaseURL + id + '/activities';
    return this.http.get<Activity[]>(url);
  }

  createTripActivity(id: number, activityForm: ActivityForm) {
    const url = this.tripsBaseURL + id + '/activities/create';
  }

  deleteTripActivity(tripId: number, activityId: number): Observable<any> {
    const url = this.tripsBaseURL + tripId + '/activities/delete/' + activityId;
    return this.http.delete(url);
  }
}
