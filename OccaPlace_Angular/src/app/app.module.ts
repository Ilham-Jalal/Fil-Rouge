import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './features1/login/login.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
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
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { FooterComponent } from './features1/footer/footer.component';
import { NgOptimizedImage } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CategoriesComponent } from './features1/categories/categories.component';
import { Features2Component } from './features1/categories/features2/features2.component';
import { Header2Component } from './features1/header2/header2.component';
import { Hero2Component } from './features1/hero2/hero2.component';
import { AnnonceDetailsComponent } from './features1/annonce-details/annonce-details.component';
import { AnnonceDialogComponent } from './features1/annonce-dialog/annonce-dialog.component';

// Importation des modules MatDialog et MatSelect
import { MatDialogModule } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import {MatNativeDateModule, MatOptionModule} from '@angular/material/core';
import { MessageComponent } from './features1/message/message.component';
import { ConversationComponent } from './features1/conversation/conversation.component';
import { SidebarComponent } from './features1/dashboard/sidebar/sidebar.component';
import {MatSidenav, MatSidenavContainer, MatSidenavContent, MatSidenavModule} from "@angular/material/sidenav";
import {MatListItem, MatNavList} from "@angular/material/list";
import { MainComponent } from './features1/dashboard/main/main.component';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from "@angular/material/table";
import { AddLivreurDialogComponent } from './features1/add-livreur-dialog/add-livreur-dialog.component';
import {DashboardComponent} from "./features1/dashboard/dashboard.component";
import { ConversationListComponent } from './features1/conversation-list/conversation-list.component';
import { ProfileComponent } from './features1/profile/profile.component';
import { LivraisonDialogComponent } from './features1/livraison-dialog/livraison-dialog.component';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import { UserLivraisonsComponent } from './features1/user-livraisons/user-livraisons.component';
import { UpdateCommentComponent } from './features1/update-comment/update-comment.component';
import { UpdateAnnonceDialogComponent } from './features1/update-annonce-dialog/update-annonce-dialog.component';
import { LivraisonListComponent } from './features1/livraison-list/livraison-list.component';

@NgModule({
  declarations: [
    AppComponent,
    AnnonceComponent,
    AnnonceListComponent,
    AnnoncesByCategoryComponent,
    HomeComponent,
    HeroComponent,
    FeaturesComponent,
    CategoriesComponent,
    Features2Component,
    AnnonceDetailsComponent,
    Hero2Component,
    AnnonceDialogComponent,
    MessageComponent,
    ConversationComponent,
    DashboardComponent,
    SidebarComponent,
    MainComponent,
    AddLivreurDialogComponent,
    ConversationListComponent,
    ProfileComponent,
    LivraisonDialogComponent,
    UserLivraisonsComponent,
    UpdateCommentComponent,
    UpdateAnnonceDialogComponent,
    LivraisonListComponent,


  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatButtonModule,
    MatGridListModule,
    MatToolbarModule,
    MatDividerModule,
    MatIconModule,
    MatDialogModule,
    MatSelectModule, // Ajoutez ce module
    MatOptionModule, // Ajoutez ce module
    NgOptimizedImage,
    LoginComponent,
    AddUserComponent,
    FooterComponent,
    AppHeaderComponent,
    MatSidenavContent,
    MatSidenavContainer,
    MatSidenav,
    MatNavList,
    MatListItem,
    MatTable,
    MatHeaderCell,
    MatColumnDef,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRowDef,
    MatRow,
    MatSidenavModule,
    MatDatepicker,
    MatDatepickerToggle,
    MatDatepickerInput,
    MatNativeDateModule,
    Header2Component,

  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
