import { Routes } from '@angular/router';
import { ApplicationOverviewComponent } from './application/application-overview/application-overview.component';
import { ApplicationDetailsComponent } from './application/application-details/application-details.component';

const appRoutes: Routes = [
  { path: '', component: ApplicationOverviewComponent },
  { path: 'applications/:id', component: ApplicationDetailsComponent }
];

export default appRoutes;
