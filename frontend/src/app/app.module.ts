import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';


import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {RouterModule, Routes} from '@angular/router';
import {SwaggerUiWrapperComponent} from './specification-version/swagger-ui-wrapper.component';
import {SpecificationOverviewComponent} from './specification-overview/specification-overview.component';
import {SpecificationFormComponent} from './specification-form/specification-form.component';
import {HeaderComponent} from './header/header.component';
import {SpecificationSearchComponent} from './specification-search/specification-search.component';
import {SpecificationSearchDetailComponent} from './specification-search-detail/specification-search-detail.component';
import {HighlightSearchResultPipe} from './pipes/highlight-search-result.pipe';
import {TrimSearchResultPipe} from './pipes/trim-search-result.pipe';
import {FormatSearchResultPipe} from './pipes/format-search-result.pipe';
import { LoginComponent } from './login/login.component';
import {AuthenticationGuard} from './guards/authentication.guard';
import {TokenInterceptor} from './interceptors/token.interceptor';
import {LoginEvent} from './login.event';
import {MarkdownModule} from 'ngx-markdown';
import {GraphiQLWrapperComponent} from './specification-version/graphiql-wrapper.component';
import {VersionViewComponent} from './specification-version/version-view.component';

const appRoutes: Routes = [
  {path: '', component: SpecificationOverviewComponent, canActivate: [AuthenticationGuard]},
  {path: 'specifications/:specificationId/:version', component: VersionViewComponent, canActivate: [AuthenticationGuard]},
  {path: 'add-specifications', component: SpecificationFormComponent, canActivate: [AuthenticationGuard]},
  {path: 'edit-specifications/:id', component: SpecificationFormComponent, canActivate: [AuthenticationGuard]},
  {path: 'search', component: SpecificationSearchDetailComponent, canActivate: [AuthenticationGuard]},
  {path: 'search/:searchString', component: SpecificationSearchDetailComponent, canActivate: [AuthenticationGuard]},
  {path: 'login', component: LoginComponent}
];


@NgModule({
  declarations: [
    AppComponent,
    SwaggerUiWrapperComponent,
    SpecificationOverviewComponent,
    SpecificationFormComponent,
    HeaderComponent,
    SpecificationSearchComponent,
    SpecificationSearchDetailComponent,
    HighlightSearchResultPipe,
    TrimSearchResultPipe,
    FormatSearchResultPipe,
    LoginComponent,
    GraphiQLWrapperComponent,
    VersionViewComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(
      appRoutes
    ),
    NgbModule.forRoot(),
    MarkdownModule.forRoot()
  ],
  providers: [
    AuthenticationGuard,
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
    LoginEvent
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
