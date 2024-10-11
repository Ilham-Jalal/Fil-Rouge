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
import {CategoriesComponent} from "./features1/categories/categories.component";
import {AnnonceDetailsComponent} from "./features1/annonce-details/annonce-details.component";
import {MessageComponent} from "./features1/message/message.component";
import {ConversationComponent} from "./features1/conversation/conversation.component";
import {SidebarComponent} from "./features1/dashboard/sidebar/sidebar.component";
import {DashboardComponent} from "./features1/dashboard/dashboard.component";
import {MainComponent} from "./features1/dashboard/main/main.component";
import {ConversationListComponent} from "./features1/conversation-list/conversation-list.component";
import {ProfileComponent} from "./features1/profile/profile.component";
import {UserLivraisonsComponent} from "./features1/user-livraisons/user-livraisons.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'add-user', component: AddUserComponent , canActivate: [AuthGuard], data: { expectedRole: Role.ADMIN }},
  { path: 'annonces', component: AnnonceComponent , canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'annonceByUser', component: AnnonceListComponent , canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'annonceByCategorie', component: AnnoncesByCategoryComponent , canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: '', component: HomeComponent, },
  { path: 'categories', component: CategoriesComponent, canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'annonce-details/:id', component: AnnonceDetailsComponent,  canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'message', component: MessageComponent, canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'conversation/:id', component: ConversationComponent, canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'conversations', component: ConversationListComponent, canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'livraison', component: UserLivraisonsComponent, canActivate: [AuthGuard], data: { expectedRole: Role.USER }},
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard], data: { expectedRole: Role.ADMIN }},
  { path: 'sidebar', component: SidebarComponent, canActivate: [AuthGuard], data: { expectedRole: Role.ADMIN }},
  { path: 'annoncesList', component: AnnonceListComponent, canActivate: [AuthGuard], data: { expectedRole: Role.ADMIN }},
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard], data: { expectedRole: Role.ADMIN },
    children: [
      {
        path: '',
        redirectTo: 'main',
        pathMatch: 'full'
      },
      {
        path: 'main',
        component: MainComponent,
        canActivate: [AuthGuard], data: { expectedRole: Role.ADMIN }
      },
      {
        path: 'annoncesList',
        component: AnnonceListComponent,
        canActivate: [AuthGuard], data: { expectedRole: Role.ADMIN }
      },
    ]
  }




];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
