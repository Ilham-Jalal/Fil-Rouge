import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./features1/login/login.component";
import {AddUserComponent} from "./features1/add-user/add-user.component";
import {Role} from "./core/enum/Role";
import {AnnonceComponent} from "./features1/annonce/annonce.component";
import {AnnonceListComponent} from "./features1/annonce-list/annonce-list.component";
import {AnnoncesByCategoryComponent} from "./features1/annonces-by-category/annonces-by-category.component";
import {HomeComponent} from "./features1/home/home.component";
import {AuthGuard} from "./core/service/autGuard.service";
import {Header2Component} from "./features1/header2/header2.component";
import {CategoriesComponent} from "./features1/categories/categories.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'add-user', component: AddUserComponent , canActivate: [AuthGuard], data: { expectedRole: Role.ADMIN }},
  { path: 'annonce', component: AnnonceComponent , canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'annonceByUser', component: AnnonceListComponent , canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'annonceByCategorie', component: AnnoncesByCategoryComponent , canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: '', component: HomeComponent},
  { path: 'categories', component: CategoriesComponent},



];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
