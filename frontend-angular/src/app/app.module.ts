import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NavigationComponent} from './navigation/navigation.component';
import {TripComponent} from './trip/trip.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {AboutComponent} from './about/about.component';
import {InformationComponent} from './trip/information/information.component';
import {ActivitiesComponent} from './trip/activities/activities.component';
import {ChatComponent} from './trip/chat/chat.component';
import {CommentComponent} from './trip/chat/comment/comment.component';
import {ProfileComponent} from './profile/profile.component';
import {HomeComponent} from './home/home.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthService} from './services/auth/auth.service';
import {AuthGuard} from './services/auth/auth.guard';
import {TokenInterceptor} from './services/auth/token-interceptor';
import {TripCardComponent} from './home/trip-card/trip-card.component';
import {PaginatorComponent} from './paginator/paginator.component';
import {CreateTripComponent} from './create-trip/create-trip.component';
import {AgmCoreModule} from "@agm/core";
import {MapActivityComponent} from './trip/activities/map-activity/map-activity.component';
import {IndexComponent} from './index/index.component';
import {LoggedGuard} from "./services/auth/logged.guard";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {PageNotFoundComponent} from './errors/page-not-found/page-not-found.component';
import {ModalModule} from "./modal";
import {BsDatepickerModule} from 'ngx-bootstrap/datepicker';
import {UserTripsComponent} from './user-trips/user-trips.component';
import {VerificationComponent} from './register/verification/verification.component';
import {VerificationResultComponent} from './register/verification-result/verification-result.component';
import {NgxSpinnerModule} from "ngx-bootstrap-spinner";
import {RespondJoinReqComponent} from './trip/respond-join-req/respond-join-req.component';
import { RespondInviteComponent } from './trip/respond-invite/respond-invite.component';
import { UserSearchComponent } from './trip/information/user-search/user-search.component';


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
        ProfileComponent,
        HomeComponent,
        TripCardComponent,
        PaginatorComponent,
        CreateTripComponent,
        MapActivityComponent,
        IndexComponent,
        PageNotFoundComponent,
        UserTripsComponent,
        VerificationComponent,
        VerificationResultComponent,
        RespondJoinReqComponent,
        RespondInviteComponent,
        UserSearchComponent,

    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        ModalModule,
        NgxSpinnerModule,
        BsDatepickerModule.forRoot(),
        AgmCoreModule.forRoot(
            {
                apiKey: 'AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M',
                libraries: ["places"]
            }
        ),
        BrowserAnimationsModule
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [AuthService, AuthGuard, {
        provide: HTTP_INTERCEPTORS,
        useClass: TokenInterceptor,
        multi: true
    }, LoggedGuard],
    bootstrap: [AppComponent]
})
export class AppModule {
}
