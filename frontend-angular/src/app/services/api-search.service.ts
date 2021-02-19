import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Trip} from "../model/trip";
import {HttpClient, HttpParams} from "@angular/common/http";
import {User} from "../model/user";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ApiSearchService {

  constructor(private http: HttpClient) {}

  private searchBaseURL = `${environment.apiURL}/search/`;

  searchTripsByName(name: string, page: number): Observable<any> {
    const url = this.searchBaseURL + 'trips';
    const params = new HttpParams().set('nameInput', name).set('page', String(page));
    return this.http.get(url, {params});
  }

  searchInvitableUsersByName(name: string, tripId: number): Observable<User>{
    const url = this.searchBaseURL + tripId + "/users";
    let params = new HttpParams().set("name", name);
    return this.http.get<User>(url, {params: params});
  }

  advancedSearch(formData: FormData): Observable<Trip[]> {
    const url = this.searchBaseURL + 'advanced';
    let params = new HttpParams();
    if (formData.get('name'))
      params = params.set('name', formData.get('name').toString());
    if (formData.get('place'))
      params = params.set('place', formData.get('place').toString());
    if (formData.get('startDate'))
      params = params.set('startDate', formData.get('startDate').toString());
    if (formData.get('endDate'))
      params = params.set('endDate', formData.get('endDate').toString());
    return this.http.get<Trip[]>(url, {params});
  }

}
