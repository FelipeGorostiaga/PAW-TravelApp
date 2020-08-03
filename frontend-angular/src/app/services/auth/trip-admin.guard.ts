import { Injectable } from '@angular/core';
import {CanActivate, Router, ActivatedRoute} from '@angular/router';
import { Observable } from 'rxjs';
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class TripAdminGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) {

  }

  // TODO
  canActivate() {
    return true;
    const tripId = this.route.snapshot.paramMap.get('id');
    this.router.navigate(['no-auth']);
    return false;
  }

}
