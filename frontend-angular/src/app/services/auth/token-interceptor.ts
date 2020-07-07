import {Injectable} from '@angular/core';
import {HttpInterceptor} from '@angular/common/http';
import {AuthService} from './auth.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(public authService: AuthService) {
    }

    intercept(req, next) {
        if (this.authService.isLoggedIn()) {
            const tokenizedReq = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${this.authService.getJwtToken()}`
                }
            });
            return next.handle(tokenizedReq);
        }
        return next.handle(req);
    }
}
