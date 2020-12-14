import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Trip} from "../model/trip";
import {HttpClient, HttpParams} from "@angular/common/http";
import {User} from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class ApiSearchService {

  constructor(private http: HttpClient) {}

  private baseURL = 'http://localhost:8080/api';
  private searchBaseURL = `${this.baseURL}/search`;

  searchByName(name: string): Observable<Trip[]> {
    const url = this.searchBaseURL + '/name';
    const params = new HttpParams().set('nameInput', name);
    return this.http.get<Trip[]>(url, {params});
  }

  searchUserByName(name: string): Observable<User> {
    const url = this.searchBaseURL + "/users";
    let params = new HttpParams().set("name", name);
    return this.http.get<User>(url, {params: params});
  }

  advancedSearch(name?: string, sdate?: string, edate?: string, category?: string): Observable<Trip[]> {
    const url = this.searchBaseURL + '/advanced';
    let params = new HttpParams();
    if (name) {
      params = params.set('name', name);
    }
    if (sdate) {
      params = params.set('startDate', sdate);
    }
    if (edate) {
      params = params.set('endDate', edate);
    }
    if (category) {
      params = params.set('category', category);
    }
    return this.http.get<Trip[]>(url, {params});
  }

}
