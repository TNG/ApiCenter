import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationCreateComponent } from '../application-create/application-create.component';
import { Application } from '../../models/application';
import { ApplicationService } from '../../services/application.service';
import { ApplicationState } from '../store/state/application.state';
import { Store } from '@ngrx/store';
import { selectApplications } from '../store/selector/application.selectors';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { loadApplications } from '../store/actions/application.actions';

@Component({
  selector: 'app-application-overview',
  templateUrl: 'application-overview.component.html',
  styleUrls: ['application-overview.component.css']
})
export class ApplicationOverviewComponent implements OnInit {
  applications$: Observable<Application[]>;
  applicationsTableData$: Observable<MatTableDataSource<Application>>;

  displayedColumns = ['name', 'description', 'contact'];

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private store: Store<ApplicationState>,
    private dialog: MatDialog
  ) {}

  async ngOnInit() {
    this.store.dispatch(loadApplications());

    this.applicationsTableData$ = this.store
      .select(selectApplications)
      .pipe(
        map(applications => new MatTableDataSource<Application>(applications))
      );
    // this.applicationsTableData.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    // this.applicationsTableData.filter = filterValue.trim().toLowerCase();
  }

  openCreateApplicationDialog() {
    this.dialog.open(ApplicationCreateComponent);
  }
}
