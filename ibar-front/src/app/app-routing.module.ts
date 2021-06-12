import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { environment } from '../environments/environment';
import { AuthGuard } from './core/guards/auth.guard';
import { LoginComponent } from './components/login/login.component';
import { MainFrameComponent } from './components/main-frame/main-frame.component';
import { ADMIN, READER } from './core/utils/consts';
import { BrowseBooksComponent } from './components/browse-books/browse-books.component';
import { BookDetailsComponent } from './components/book-details/book-details.component';
import { BookAboutComponent } from './components/book-about/book-about.component';
import { BookReviewsComponent } from './components/book-reviews/book-reviews.component';
import { BookReadingComponent } from './components/book-reading/book-reading.component';
import { PurchasePlanComponent } from './components/purchase-plan/purchase-plan.component';
import {BookListComponent} from './components/book-list/book-list.component';
import { RegistrationComponent } from './components/registration/registration.component';
import {AuthorListComponent} from './components/author-list/author-list.component';
import {CategoryListComponent} from './components/category-list/category-list.component';
import {AdminListComponent} from './components/admin-list/admin-list.component';
import {BookCreateComponent} from './components/book-create/book-create.component';

const routes: Routes = [
  {
    path: environment.loginRoute,
    component: LoginComponent,
    data: { unauthorized: true },
    canActivate: [AuthGuard]
  },
  {
    path: 'registration',
    component: RegistrationComponent,
    data: { unauthorized: true },
    canActivate: [AuthGuard]
  },
  {
    path: 'reading/:id',
    component: BookReadingComponent
  },
  {
    path: '',
    component: MainFrameComponent,
    children: [
      {
        path: '',
        redirectTo: 'browse',
        pathMatch: 'full'
      },
      {
        path: 'browse',
        component: BrowseBooksComponent,
        data: { roles: [READER] },
        canActivate: [AuthGuard],
      },
      {
        path: 'book/:id',
        component: BookDetailsComponent,
        data: { roles: [ADMIN, READER] },
        canActivate: [AuthGuard],
        children: [
          { path: '', redirectTo: 'about', pathMatch: 'full' },
          { path: 'reviews', component: BookReviewsComponent },
          { path: 'about', component: BookAboutComponent }
        ]
      },
      {
        path: 'plan',
        data: { roles: [READER] },
        canActivate: [AuthGuard],
        component: PurchasePlanComponent,
      },
      {
        path: 'authors',
        component: AuthorListComponent,
        data: { roles: [ADMIN] },
        canActivate: [AuthGuard],
      },
      {
        path: 'list',
        component: BookListComponent,
        data: { roles: [ADMIN, READER] },
        canActivate: [AuthGuard],
      },
      {
        path: 'categories',
        component: CategoryListComponent,
        data: { roles: [ADMIN] },
        canActivate: [AuthGuard],
      },
      {
        path: 'create-book',
        component: BookCreateComponent,
        data: { roles: [ADMIN] },
        canActivate: [AuthGuard],
      },
      {
        path: 'admins',
        component: AdminListComponent,
        data: { roles: [ADMIN] },
        canActivate: [AuthGuard],
      },
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
