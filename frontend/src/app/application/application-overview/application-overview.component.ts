import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationCreateComponent } from '../application-create/application-create.component';
import { Application } from '../../models/application';
import { Store } from '@ngrx/store';
import { selectApplications } from '../store/selector/application.selectors';
import { Observable } from 'rxjs';
import { loadApplications } from '../store/actions/application.actions';
import { AppState } from '../../store/state/app.state';
import { ApplicationDeleteComponent } from '../application-delete/application-delete.component';

@Component({
  selector: 'app-application-overview',
  templateUrl: 'application-overview.component.html',
  styleUrls: ['application-overview.component.scss']
})
export class ApplicationOverviewComponent implements OnInit {
  applications$: Observable<Application[]>;

  constructor(private store: Store<AppState>, private dialog: MatDialog) {}

  async ngOnInit() {
    this.store.dispatch(loadApplications());

    this.applications$ = this.store.select(selectApplications);
  }

  openCreateApplicationDialog() {
    this.dialog.open(ApplicationCreateComponent);
  }

  openConfirmDeleteApplicationDialog(application: Application) {
    this.dialog.open(ApplicationDeleteComponent, { data: { application } });
  }
}