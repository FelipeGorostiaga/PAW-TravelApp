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
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'user-trips',
    component: UserTripsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'trip/:id',
    component: TripComponent,
    canLoad: [AuthGuard],
  },
  {
    path: 'profile/:id',
    component: ProfileComponent,
    canLoad: [AuthGuard]
  },
  {
    path: 'about',
    component: AboutComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'create-trip',
    component: CreateTripComponent,
    canLoad: [AuthGuard]
  },
  {
    path: '',
    component: IndexComponent
  },
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
