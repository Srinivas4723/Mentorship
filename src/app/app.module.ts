import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { RegisterComponent } from './register/register.component';
import { RouterModule, Routes } from '@angular/router';
import { CompensationuserComponent } from './compensationuser/compensationuser.component';
import { HomepageComponent } from './homepage/homepage.component';
import { ReportuserComponent } from './reportuser/reportuser.component';
import { LoginComponent } from './login/login.component';
import { authInterceptorProviders } from './_helpers/auth.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { AdminhomeComponent } from './adminhome/adminhome.component';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes=[

  {path:"",component:HomepageComponent},
  {path:"login",component:LoginComponent},
  {path:"reporthome",component:ReportuserComponent},
  {path:"adminhome",component:AdminhomeComponent},
  {path:"register",component:RegisterComponent},
  {path:"profile",component:ProfileComponent},
  {path:"compensationhome",component:CompensationuserComponent}
  
]
@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    CompensationuserComponent,
    HomepageComponent,
    ReportuserComponent,
    LoginComponent,
    AdminhomeComponent,
    ProfileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgxPaginationModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    RouterModule.forRoot(routes),
    BrowserAnimationsModule,
    FontAwesomeModule
  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent],
  
})
export class AppModule { }
