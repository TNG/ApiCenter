import { Routes } from '@angular/router';
import { ApplicationOverviewComponent } from './application/application-overview/application-overview.component';
import { ApplicationDetailsComponent } from './application/application-details/application-details.component';
import { InterfaceOverviewComponent } from './interface/interface-overview/interface-overview.component';
import { InterfaceDetailsComponent } from './interface/interface-details/interface-details.component';
import { VersionOverviewComponent } from './version/version-overview/version-overview.component';
import { VersionDetailsComponent } from './version/version-details/version-details.component';

export const appRoutes: Routes = [
  { path: '', component: ApplicationOverviewComponent },
  { path: 'applications', component: ApplicationOverviewComponent },
  { path: 'applications/:id', component: ApplicationDetailsComponent },
  { path: 'interfaces', component: InterfaceOverviewComponent },
  { path: 'interfaces/:id', component: InterfaceDetailsComponent },
  { path: 'versions', component: VersionOverviewComponent },
  { path: 'versions/:id', component: VersionDetailsComponent }
];
