import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { TripComponent } from './trip/trip.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AboutComponent } from './about/about.component';
import { InformationComponent } from './trip/information/information.component';
import { ActivitiesComponent } from './trip/activities/activities.component';
import { ChatComponent } from './trip/chat/chat.component';
import { CommentComponent } from './trip/chat/comment/comment.component';
import { MapComponent } from './map/map.component';
import { ProfileComponent } from './profile/profile.component';
import { HomeComponent } from './home/home.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from './services/auth/auth.service';
import { AuthGuard} from './services/auth/auth.guard';
import { TokenInterceptor } from './services/auth/token-interceptor';
import { TripCardComponent } from './home/trip-card/trip-card.component';
import { PaginatorComponent } from './paginator/paginator.component';
import { CreateTripComponent } from './create-trip/create-trip.component';
import { AgmCoreModule } from "@agm/core";
import { MapActivityComponent } from './trip/activities/map-activity/map-activity.component';
import { IndexComponent } from './index/index.component';
import { LoggedGuard } from "./services/auth/logged.guard";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CreateActivityComponent } from './create-activity/create-activity.component';
import { PageNotFoundComponent } from './errors/page-not-found/page-not-found.component';
import { ModalModule } from "./modal";
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    TripComponent,
    LoginComponent,
    RegisterComponent,
    AboutComponent,
    InformationComponent,
    ActivitiesComponent,
    ChatComponent,
    CommentComponent,
    MapComponent,
    ProfileComponent,
    HomeComponent,
    TripCardComponent,
    PaginatorComponent,
    CreateTripComponent,
    MapActivityComponent,
    IndexComponent,
    CreateActivityComponent,
    PageNotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    ModalModule,
    BsDatepickerModule.forRoot(),
    AgmCoreModule.forRoot(
        {
          apiKey: 'AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M',
          libraries: ["places"]
        }
    ),
    BrowserAnimationsModule
  ],
  providers: [AuthService, AuthGuard, {
    provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptor,
    multi: true
  }, LoggedGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
