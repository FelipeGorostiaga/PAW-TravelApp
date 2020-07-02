import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

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
    ProfileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
