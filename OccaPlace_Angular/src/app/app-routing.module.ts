import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {SignUpComponent} from "./sign-up/sign-up.component";
import {AddUserComponent} from "./add-user/add-user.component";
import {AuthGuard} from "./service/autGuard.service";
import {Role} from "./enum/Role";
import {AnnonceComponent} from "./annonce/annonce.component";
import {AnnonceListComponent} from "./annonce-list/annonce-list.component";
import {AnnoncesByCategoryComponent} from "./annonces-by-category/annonces-by-category.component";
import {HomeComponent} from "./home/home.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'add-user', component: AddUserComponent , canActivate: [AuthGuard], data: { expectedRole: Role.ADMIN }},
  { path: 'annonce', component: AnnonceComponent , canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'annonceByUser', component: AnnonceListComponent , canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'annonceByCategorie', component: AnnoncesByCategoryComponent , canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: '', component: HomeComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
