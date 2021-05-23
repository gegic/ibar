import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {JwtInterceptor} from './core/interceptors/jwt.interceptor';
import {ConfirmationService, MessageService} from 'primeng/api';
import {DialogService} from 'primeng/dynamicdialog';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { LoginComponent } from './components/login/login.component';
import {InputTextModule} from 'primeng/inputtext';
import {PasswordModule} from 'primeng/password';
import {ButtonModule} from 'primeng/button';
import {CardModule} from 'primeng/card';
import {MenubarModule} from 'primeng/menubar';
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import {ToastModule} from 'primeng/toast';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {AppRoutingModule} from './app-routing.module';
import {MenuModule} from 'primeng/menu';
import { MainFrameComponent } from './components/main-frame/main-frame.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import {AvatarModule} from 'primeng/avatar';
import { BrowseBooksComponent } from './components/browse-books/browse-books.component';
import {CarouselModule} from 'primeng/carousel';
import { BookCoverComponent } from './components/book-cover/book-cover.component';
import { TruncatePipe } from './core/pipes/truncate.pipe';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MainFrameComponent,
    NavbarComponent,
    BrowseBooksComponent,
    BookCoverComponent,
    TruncatePipe
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    CardModule,
    MenuModule,
    MenubarModule,
    ProgressSpinnerModule,
    ToastModule,
    ConfirmDialogModule,
    AppRoutingModule,
    AvatarModule,
    CarouselModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    },
    ConfirmationService,
    MessageService,
    DialogService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
