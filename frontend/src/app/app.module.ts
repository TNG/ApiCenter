import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';


import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {RouterModule, Routes} from '@angular/router';
import {SpecificationVersionComponent} from './specification-version/specification-version.component';
import {SpecificationOverviewComponent} from './specification-overview/specification-overview.component';
import {SpecificationFormComponent} from './specification-form/specification-form.component';
import {HeaderComponent} from './header/header.component';
import {SpecificationSearchComponent} from './specification-search/specification-search.component';
import {SpecificationSearchDetailComponent} from './specification-search-detail/specification-search-detail.component';
import {HighlightSearchResultPipe} from './pipes/highlight-search-result.pipe';
import {TrimSearchResultPipe} from './pipes/trim-search-result.pipe';
import {FormatSearchResultPipe} from './pipes/format-search-result.pipe';

const appRoutes: Routes = [
  {path: '', component: SpecificationOverviewComponent},
  {path: 'specifications/versions/:id', component: SpecificationVersionComponent},
  {path: 'specifications/form/add', component: SpecificationFormComponent},
  {path: 'specifications/form/edit/:id', component: SpecificationFormComponent},
  {path: 'search', component: SpecificationSearchDetailComponent},
  {path: 'search/:searchString', component: SpecificationSearchDetailComponent}
];


@NgModule({
  declarations: [
    AppComponent,
    SpecificationVersionComponent,
    SpecificationOverviewComponent,
    SpecificationFormComponent,
    HeaderComponent,
    SpecificationSearchComponent,
    SpecificationSearchDetailComponent,
    HighlightSearchResultPipe,
    TrimSearchResultPipe,
    FormatSearchResultPipe
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(
      appRoutes
    ),
    NgbModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
