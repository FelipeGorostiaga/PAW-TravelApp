import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Place} from "../model/place";

@Injectable({
  providedIn: 'root'
})
export class ApiPlaceService {

  private baseURL = 'http://localhost:8080/api';
  private placesBaseURL = `${this.baseURL}/places`;

  constructor(private http: HttpClient) { }

  getPlaceById(id: number): Observable<Place> {
    const url = this.placesBaseURL + '/' + id;
    return this.http.get<Place>(url);
  }
}
