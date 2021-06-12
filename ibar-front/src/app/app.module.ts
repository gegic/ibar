import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { JwtInterceptor } from './core/interceptors/jwt.interceptor';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './components/login/login.component';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MenubarModule } from 'primeng/menubar';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { AppRoutingModule } from './app-routing.module';
import { MenuModule } from 'primeng/menu';
import { MainFrameComponent } from './components/main-frame/main-frame.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { AvatarModule } from 'primeng/avatar';
import { BrowseBooksComponent } from './components/browse-books/browse-books.component';
import { CarouselModule } from 'primeng/carousel';
import { BookCoverComponent } from './components/book-cover/book-cover.component';
import { TruncatePipe } from './core/pipes/truncate.pipe';
import { SkeletonModule } from 'primeng/skeleton';
import { CalendarModule } from 'primeng/calendar';
import { BookDetailsComponent } from './components/book-details/book-details.component';
import { DetailsNavigationComponent } from './components/details-navigation/details-navigation.component';
import { BookAboutComponent } from './components/book-about/book-about.component';
import { BookReviewsComponent } from './components/book-reviews/book-reviews.component';
import { RatingModule } from 'primeng/rating';
import { ProgressBarModule } from 'primeng/progressbar';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { DialogModule } from 'primeng/dialog';
import { BookReviewElementComponent } from './components/book-review-element/book-review-element.component';
import { RippleModule } from 'primeng/ripple';
import { BookReadingComponent } from './components/book-reading/book-reading.component';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { PlanInterceptor } from './core/interceptors/plan.interceptor';
import { PurchasePlanComponent } from './components/purchase-plan/purchase-plan.component';
import { PlanCoverComponent } from './components/plan-cover/plan-cover.component';
import { CategoryComponent } from './components/category/category.component';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { BookListComponent } from './components/book-list/book-list.component';
import { AdminComponent } from './components/admin/admin.component';
import { AdminListComponent } from './components/admin-list/admin-list.component';
import {SliderModule} from 'primeng/slider';
import { BookListElementComponent } from './components/book-list-element/book-list-element.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { AuthorComponent } from './components/author/author.component';
import { AuthorListComponent } from './components/author-list/author-list.component';
import { FileUploadModule } from 'primeng/fileupload';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { MessageModule } from 'primeng/message';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MainFrameComponent,
    NavbarComponent,
    BrowseBooksComponent,
    BookCoverComponent,
    TruncatePipe,
    BookDetailsComponent,
    DetailsNavigationComponent,
    BookAboutComponent,
    BookReviewsComponent,
    BookReviewElementComponent,
    BookReadingComponent,
    PurchasePlanComponent,
    PlanCoverComponent,
    CategoryComponent,
    CategoryListComponent,
    AdminComponent,
    AdminListComponent,
    BookListComponent,
    BookListElementComponent,
    RegistrationComponent,
    AuthorComponent,
    AuthorListComponent,
    ChangePasswordComponent,
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
    CarouselModule,
    SkeletonModule,
    RatingModule,
    ProgressBarModule,
    InfiniteScrollModule,
    InputTextareaModule,
    DialogModule,
    RippleModule,
    NgxExtendedPdfViewerModule,
    SliderModule,
    CalendarModule,
    FileUploadModule,
    RadioButtonModule,
    MessageModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: PlanInterceptor,
      multi: true
    },
    ConfirmationService,
    MessageService,
    DialogService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
