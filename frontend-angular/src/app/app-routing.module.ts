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


const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/login'
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'home',
    component: HomeComponent,
    canLoad: [LoggedGuard]
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
    path: 'about',
    component: AboutComponent
  },
  {
    path: 'create-trip',
    component: CreateTripComponent,
    canLoad: [LoggedGuard]
  },
  {
    path: 'index',
    component: IndexComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
