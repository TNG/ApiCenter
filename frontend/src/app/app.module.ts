import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {RouterModule, Routes} from '@angular/router';
import { SpecificationComponent } from './specification/specification.component';
import { SpecificationOverviewComponent } from './specification-overview/specification-overview.component';
import { SpecificationFormComponent } from './specification-form/specification-form.component';
import { HeaderComponent } from './header/header.component';
import {NgSelectModule} from "@ng-select/ng-select";
import { SpecificationSearchComponent } from './specification-search/specification-search.component';

const appRoutes: Routes = [
  { path:  '', component: SpecificationOverviewComponent },
  { path: 'specifications/:id', component: SpecificationComponent },
  { path: 'specifications/form/add', component: SpecificationFormComponent },
  { path: 'specifications/form/edit/:id', component: SpecificationFormComponent }
];


@NgModule({
  declarations: [
    AppComponent,
    SpecificationComponent,
    SpecificationOverviewComponent,
    SpecificationFormComponent,
    HeaderComponent,
    SpecificationSearchComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(
      appRoutes
    ),
    NgbModule.forRoot(),
    NgSelectModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
