import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HTTP_INTERCEPTORS, HttpClientModule, provideHttpClient } from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AuthInterceptor } from './interceptor/auth-interceptor';
import { SignUpComponent } from './sign-up/sign-up.component';
import { AddUserComponent } from './add-user/add-user.component';
import { AnnonceComponent } from './annonce/annonce.component';

@NgModule({
  declarations: [
    AppComponent,
    AnnonceComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    LoginComponent,
    SignUpComponent,
    AddUserComponent,
    AddUserComponent,
    AddUserComponent,
    FormsModule
  ],

    providers: [
      { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
    ],



  bootstrap: [AppComponent]
})
export class AppModule {}
