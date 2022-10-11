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

const routes: Routes=[

  {path:"",component:HomepageComponent},
  {path:"login",component:LoginComponent},
  {path:"reporthome",component:ReportuserComponent},
  {path:"register",component:RegisterComponent},
  {path:"compensationhome",component:CompensationuserComponent}
  
]
@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    CompensationuserComponent,
    HomepageComponent,
    ReportuserComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgxPaginationModule,
    RouterModule.forRoot(routes)
  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
