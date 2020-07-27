import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {HomeComponent} from './home/home.component';
import {TripComponent} from './trip/trip.component';
import {AuthGuard} from './services/auth/auth.guard';
import {AboutComponent} from "./about/about.component";
import {CreateTripComponent} from "./create-trip/create-trip.component";
import {IndexComponent} from "./index/index.component";
import {LoggedGuard} from "./services/auth/logged.guard";
import {ProfileComponent} from "./profile/profile.component";


const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [LoggedGuard]
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/login'
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'trip/:id',
    component: TripComponent,
    canLoad: [LoggedGuard]
  },
  {
    path: 'profile/:id',
    component: ProfileComponent,
    canLoad: [LoggedGuard]
  },
  {
    path: 'about',
    component: AboutComponent,
    canActivate: [LoggedGuard]
  },
  {
    path: 'create-trip',
    component: CreateTripComponent,
    canLoad: [LoggedGuard]
  },
  {
    path: 'index',
    component: IndexComponent
  },
  { path:   '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
