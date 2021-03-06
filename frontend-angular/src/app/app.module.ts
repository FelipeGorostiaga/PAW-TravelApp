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
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {PageNotFoundComponent} from './errors/page-not-found/page-not-found.component';
import {ModalModuleCustom} from "./modal";
import {BsDatepickerModule} from 'ngx-bootstrap/datepicker';
import {UserTripsComponent} from './user-trips/user-trips.component';
import {VerificationComponent} from './register/verification/verification.component';
import {VerificationResultComponent} from './register/verification-result/verification-result.component';
import {NgxSpinnerModule} from "ngx-bootstrap-spinner";
import {RespondJoinReqComponent} from './trip/respond-join-req/respond-join-req.component';
import {RespondInviteComponent} from './trip/respond-invite/respond-invite.component';
import {UserListItemComponent} from './trip/members/user-list-item/user-list-item.component';
import {AlertModule} from "ngx-bootstrap/alert";
import {ModalModule} from "ngx-bootstrap/modal";
import {TooltipModule} from 'ngx-bootstrap/tooltip';
import {AdvancedSearchComponent} from './advanced-search/advanced-search.component';
import {RatingModule} from 'ngx-bootstrap/rating';
import {ForbiddenComponent} from './errors/forbidden/forbidden.component';
import {RateTileComponent} from './profile/rates/rate-tile/rate-tile.component';
import {SearchResultComponent} from './search-result/search-result.component';
import {ConfirmationPopoverModule} from 'angular-confirmation-popover';
import {RatesComponent} from './profile/rates/rates.component';
import {PendingRatesComponent} from './profile/pending-rates/pending-rates.component';
import {InvitationsComponent} from './profile/invitations/invitations.component';
import {RateFormTileComponent} from './profile/pending-rates/rate-form-tile/rate-form-tile.component';
import {UserRatesComponent} from './user-rates/user-rates.component';
import {MembersComponent} from './trip/members/members.component';


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
        UserListItemComponent,
        AdvancedSearchComponent,
        ForbiddenComponent,
        RateTileComponent,
        SearchResultComponent,
        RatesComponent,
        PendingRatesComponent,
        InvitationsComponent,
        RateFormTileComponent,
        UserRatesComponent,
        MembersComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        ModalModuleCustom,
        NgxSpinnerModule,
        BsDatepickerModule.forRoot(),
        AgmCoreModule.forRoot(
            {
                apiKey: 'AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M',
                libraries: ["places"]
            }
        ),
        BrowserAnimationsModule,
        AlertModule.forRoot(),
        ModalModule.forRoot(),
        TooltipModule.forRoot(),
        RatingModule.forRoot(),
        ConfirmationPopoverModule.forRoot({
            confirmButtonType: 'primary',
        })
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [AuthService, AuthGuard, {
        provide: HTTP_INTERCEPTORS,
        useClass: TokenInterceptor,
        multi: true
    }],
    bootstrap: [AppComponent]
})
export class AppModule {
}
