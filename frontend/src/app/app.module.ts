import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';


import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {RouterModule, Routes} from '@angular/router';
import {SpecificationComponent} from './specification/specification.component';
import {SpecificationOverviewComponent} from './specification-overview/specification-overview.component';
import {SpecificationFormComponent} from './specification-form/specification-form.component';
import {HeaderComponent} from './header/header.component';
import {SpecificationSearchComponent} from './specification-search/specification-search.component';
import {SpecificationSearchDetailComponent} from './specification-search-detail/specification-search-detail.component';
import {HighlightSearchResultPipe} from './pipes/highlight-search-result.pipe';
import {TrimSearchResultPipe} from './pipes/trim-search-result.pipe';
import {FormatSearchResultPipe} from './pipes/format-search-result.pipe';
import { LoginComponent } from './login/login.component';
import {AuthenticationGuard} from "./guards/authentication.guard";
import {TokenInterceptor} from "./interceptors/token.interceptor";

const appRoutes: Routes = [
  {path: '', component: SpecificationOverviewComponent, canActivate: [AuthenticationGuard]},
  {path: 'specifications/:id', component: SpecificationComponent, canActivate: [AuthenticationGuard]},
  {path: 'specifications/form/add', component: SpecificationFormComponent, canActivate: [AuthenticationGuard]},
  {path: 'specifications/form/edit/:id', component: SpecificationFormComponent, canActivate: [AuthenticationGuard]},
  {path: 'search', component: SpecificationSearchDetailComponent, canActivate: [AuthenticationGuard]},
  {path: 'search/:searchString', component: SpecificationSearchDetailComponent, canActivate: [AuthenticationGuard]},
  {path: 'login', component: LoginComponent}
];


@NgModule({
  declarations: [
    AppComponent,
    SpecificationComponent,
    SpecificationOverviewComponent,
    SpecificationFormComponent,
    HeaderComponent,
    SpecificationSearchComponent,
    SpecificationSearchDetailComponent,
    HighlightSearchResultPipe,
    TrimSearchResultPipe,
    FormatSearchResultPipe,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(
      appRoutes
    ),
    NgbModule.forRoot()
  ],
  providers: [
    AuthenticationGuard,
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
