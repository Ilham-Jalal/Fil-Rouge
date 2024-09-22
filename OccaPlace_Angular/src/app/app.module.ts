import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './features1/login/login.component';
import { HTTP_INTERCEPTORS, HttpClientModule, provideHttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthInterceptor } from './core/interceptor/auth-interceptor';
import { AddUserComponent } from './features1/add-user/add-user.component';
import { AnnonceComponent } from './features1/annonce/annonce.component';
import { AnnonceListComponent } from './features1/annonce-list/annonce-list.component';
import { AnnoncesByCategoryComponent } from './features1/annonces-by-category/annonces-by-category.component';
import { HomeComponent } from './features1/home/home.component';
import { AppHeaderComponent } from './features1/home/app-header/app-header.component';
import { HeroComponent } from './features1/home/hero/hero.component';
import { FeaturesComponent } from './features1/home/features/features.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

// Importations de Angular Material
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatDividerModule } from "@angular/material/divider";
import { MatIconModule } from "@angular/material/icon";
import { FooterComponent } from './features1/footer/footer.component';
import {NgOptimizedImage} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import { CategoriesComponent } from './features1/categories/categories.component';
import { Features2Component } from './features1/categories/features2/features2.component';
import {Header2Component} from "./features1/header2/header2.component";
import { Hero2Component } from './features1/categories/hero2/hero2.component';

@NgModule({
  declarations: [
    AppComponent,
    AnnonceComponent,
    AnnonceListComponent,
    AnnoncesByCategoryComponent,
    HomeComponent,
    AppHeaderComponent,
    HeroComponent,
    FeaturesComponent,
    FooterComponent,
    CategoriesComponent,
    Features2Component,
    Hero2Component,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    BrowserModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatButtonModule,
    MatCardModule,
    MatButtonModule,
    MatGridListModule,
    MatToolbarModule,
    MatDividerModule,
    MatIconModule,
    AddUserComponent,
    LoginComponent,
    NgOptimizedImage,
    Header2Component
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
