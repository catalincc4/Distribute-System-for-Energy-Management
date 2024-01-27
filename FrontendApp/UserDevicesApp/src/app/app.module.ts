import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import {FormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomePageComponent } from './components/home-page/home-page.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import { GeneralLayoutComponent } from './components/general-layout/general-layout.component';
import { NavBarComponent } from './components/general-layout/nav-bar/nav-bar.component';
import { EditModelComponent } from './components/edit-model/edit-model.component';
import {CdkOption} from "@angular/cdk/listbox";
import {MatSelectModule} from "@angular/material/select";
import {DatePipe, HashLocationStrategy, LocationStrategy} from "@angular/common";
import { UserCardComponent } from './components/user-card/user-card.component';
import { DeviceCardComponent } from './components/device-card/device-card.component';
import {CalendarModule} from "primeng/calendar";
import {NgApexchartsModule} from "ng-apexcharts";
import { MessangerComponent } from './components/messanger/messanger.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomePageComponent,
    GeneralLayoutComponent,
    NavBarComponent,
    EditModelComponent,
    UserCardComponent,
    DeviceCardComponent,
    MessangerComponent
  ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        FormsModule,
        MatInputModule,
        HttpClientModule,
        CdkOption,
        MatSelectModule,
        NgApexchartsModule,
        CalendarModule,
        MatCheckboxModule
    ],
  providers: [   HttpClient,
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
