import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {HomeComponent} from './home/home.component';
import {TripComponent} from './trip/trip.component';
import {AuthGuard} from './services/auth/auth.guard';
import {AboutComponent} from "./about/about.component";
import {CreateTripComponent} from "./create-trip/create-trip.component";
import {IndexComponent} from "./index/index.component";
import {ProfileComponent} from "./profile/profile.component";
import {PageNotFoundComponent} from "./errors/page-not-found/page-not-found.component";
import {UserTripsComponent} from "./user-trips/user-trips.component";
import {VerificationComponent} from "./register/verification/verification.component";
import {VerificationResultComponent} from "./register/verification-result/verification-result.component";
import {RespondJoinReqComponent} from "./trip/respond-join-req/respond-join-req.component";
import {RespondInviteComponent} from "./trip/respond-invite/respond-invite.component";
import {UserRatesComponent} from "./user-rates/user-rates.component";
import {UserNotificationsComponent} from "./user-notifications/user-notifications.component";
import {CompleteRatesComponent} from "./complete-rates/complete-rates.component";
import {ForbiddenComponent} from "./errors/forbidden/forbidden.component";
import {SearchResultComponent} from "./search-result/search-result.component";

const routes: Routes = [
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'register',
        component: RegisterComponent
    },
    {
        path: 'verification',
        component: VerificationComponent
    },
    {
        path: 'verify/:code',
        component: VerificationResultComponent
    },
    {
        path: 'home',
        component: HomeComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'trip/:id/invite',
        component: RespondInviteComponent,
        canActivate: [AuthGuard],
    },
    {
        path: 'trip/:id/joinRequest',
        component: RespondJoinReqComponent,
        canActivate: [AuthGuard],
    },
    {
        path: 'user-trips',
        component: UserTripsComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'trip/:id',
        component: TripComponent,
        canActivate: [AuthGuard],
    },
    {
        path: 'profile/:id',
        component: ProfileComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'about',
        component: AboutComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'search-result',
        component: SearchResultComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'create-trip',
        component: CreateTripComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'profile/:id/rates',
        component: UserRatesComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'user/:id/notifications',
        component: UserNotificationsComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'user/:id/pending/rates',
        component: CompleteRatesComponent,
        canActivate: [AuthGuard]
    },
    {
        path: '',
        component: IndexComponent
    },
    {
        path: 'forbidden',
        component: ForbiddenComponent
    },
    {
        path: '404',
        component: PageNotFoundComponent
    },
    {path: '**', component: PageNotFoundComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
