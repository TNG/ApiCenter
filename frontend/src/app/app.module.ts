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
import {LoginComponent} from './login/login.component';
import {AuthenticationGuard} from './auth/auth.guard';
import {AuthenticationService} from './auth/auth.service';
import {TokenInterceptor} from './interceptors/token.interceptor';
import {LoginEvent} from './login.event';
import {MarkdownModule} from 'ngx-markdown';
import {GraphiQLWrapperComponent} from './specification-version/graphiql-wrapper.component';
import {SpecificationViewComponent} from './specification-version/specification-view.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {DiffVersionsComponent} from './diff-versions/diff-versions.component';
import {PermissionsFormComponent} from './permissions-form/permissions-form.component';

const appRoutes: Routes = [
  {path: '', component: SpecificationOverviewComponent},
  {path: 'specifications/:serviceId/:version', component: SpecificationViewComponent, canActivate: [AuthenticationGuard]},
  {path: 'diff/:serviceId', component: DiffVersionsComponent, canActivate: [AuthenticationGuard]},
  {path: 'edit-permissions/:serviceId', component: PermissionsFormComponent, canActivate: [AuthenticationGuard]},
  {path: 'add-specifications', component: SpecificationFormComponent, canActivate: [AuthenticationGuard]},
  {path: 'edit-specifications/:serviceId', component: SpecificationFormComponent, canActivate: [AuthenticationGuard]},
  {path: 'search', component: SpecificationSearchDetailComponent},
  {path: 'search/:searchString', component: SpecificationSearchDetailComponent},
  {path: 'login', component: LoginComponent}
];


@NgModule({
  declarations: [
    AppComponent,
    SwaggerUiWrapperComponent,
    SpecificationOverviewComponent,
    SpecificationFormComponent,
    PermissionsFormComponent,
    DiffVersionsComponent,
    HeaderComponent,
    SpecificationSearchComponent,
    SpecificationSearchDetailComponent,
    HighlightSearchResultPipe,
    TrimSearchResultPipe,
    FormatSearchResultPipe,
    LoginComponent,
    GraphiQLWrapperComponent,
    SpecificationViewComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(
      appRoutes
    ),
    NgbModule,
    MarkdownModule.forRoot(),
    BrowserAnimationsModule
  ],
  providers: [
    AuthenticationGuard,
    AuthenticationService,
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
    LoginEvent
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
