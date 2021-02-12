import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, Subject, empty, throwError} from "rxjs";
import {AuthService} from "./auth.service";
import {catchError, switchMap, tap} from "rxjs/operators";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(private authService: AuthService) {
    }

    refreshingAccessToken = false;
    accessTokenRefreshed: Subject<any> = new Subject();

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<any> {

        //Handle the request
        request = this.addAuthHeader(request);

        return next.handle(request).pipe(
            catchError((error: HttpErrorResponse) => {
                //Unauthorized
                if (error.status === 401 && !this.refreshingAccessToken) {
                    //refresh the access token
                    return this.refreshAccessToken()
                        .pipe(
                            switchMap(() => {
                                request = this.addAuthHeader(request);
                                return next.handle(request);
                            }),
                            catchError((err: any) => {
                                this.authService.logout();
                                return empty();
                            })
                        );
                }
                return throwError(error);
            })
        );

    }

    refreshAccessToken() {
        if (this.refreshingAccessToken) {
            return new Observable(observer => {
                this.accessTokenRefreshed.subscribe(() => {
                    // this code will run when the access token has been refreshed
                    observer.next();
                    observer.complete();
                });
            });
        } else {
            this.refreshingAccessToken = true;
            // we want to call a method in the auth service to send a request to refresh the access token
            return this.authService.refreshToken().pipe(
                tap(() => {
                    this.refreshingAccessToken = false;
                    this.accessTokenRefreshed.next();
                })
            );
        }

    }

    addAuthHeader(request: HttpRequest<any>) {
        // get the access token
        const token = this.authService.getAccessToken();
        if (token) {
            // append the access token to the request header
            return request.clone({
                headers: request.headers.set("Authorization",
                    "Bearer " + token)
            });
        }
        return request;
    }
}
