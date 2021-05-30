import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {environment} from '../environments/environment';
import {AuthGuard} from './core/guards/auth.guard';
import {LoginComponent} from './components/login/login.component';
import {MainFrameComponent} from './components/main-frame/main-frame.component';
import {ADMIN, READER} from './core/utils/consts';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowseBooksComponent} from './components/browse-books/browse-books.component';
import {BookDetailsComponent} from './components/book-details/book-details.component';
import {BookAboutComponent} from './components/book-about/book-about.component';
import {BookReviewsComponent} from './components/book-reviews/book-reviews.component';
import {BookReadingComponent} from './components/book-reading/book-reading.component';

const routes: Routes = [
  {
    path: environment.loginRoute,
    component: LoginComponent,
    data: {unauthorized: true},
    canActivate: [AuthGuard]
  },
  {
    path: 'reading/:id',
    component: BookReadingComponent
  },
  {
    path: '',
    component: MainFrameComponent,
    data: {roles: [ADMIN, READER]},
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        redirectTo: 'browse',
        pathMatch: 'full'
      },
      {
        path: 'browse',
        component: BrowseBooksComponent
      },
      {
        path: 'book/:id',
        component: BookDetailsComponent,
        children: [
          { path: '', redirectTo: 'about', pathMatch: 'full'},
          { path: 'reviews', component: BookReviewsComponent },
          { path: 'about', component: BookAboutComponent }
        ]
      }
    ]
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: environment.loginRoute
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
