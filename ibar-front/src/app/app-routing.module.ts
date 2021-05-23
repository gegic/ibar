import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {environment} from '../environments/environment';
import {AuthGuard} from './core/guards/auth.guard';
import {LoginComponent} from './components/login/login.component';
import {MainFrameComponent} from './components/main-frame/main-frame.component';
import {ADMIN, READER} from './core/utils/consts';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowseBooksComponent} from './components/browse-books/browse-books.component';

const routes: Routes = [
  {
    path: environment.loginRoute,
    component: LoginComponent,
    data: {unauthorized: true},
    canActivate: [AuthGuard]
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
        component: BrowseBooksComponent,
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