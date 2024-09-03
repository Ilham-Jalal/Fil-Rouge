import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {SignUpComponent} from "./sign-up/sign-up.component";
import {AddUserComponent} from "./add-user/add-user.component";
import {AuthGuard} from "./service/autGuard.service";
import {Role} from "./enum/Role";

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'add-user', component: AddUserComponent , canActivate: [AuthGuard], data: { expectedRole: Role.ADMIN }},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
