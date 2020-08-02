import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Version } from '../../models/version';
import { Interface } from '../../models/interface';
import { Application } from '../../models/application';
import { loadInterfaces } from '../../interface/store/actions/interface.actions';
import { loadApplications } from '../../application/store/actions/application.actions';
import { Store } from '@ngrx/store';
import { AppState } from '../../store/state/app.state';
import { selectVersionsWithInterfaceAndApplication } from '../store/selectors/version.selectors';
import { loadVersions } from '../store/actions/version.actions';
import { Router } from '@angular/router';

@Component({
  selector: 'app-version-overview',
  templateUrl: './version-overview.component.html',
  styleUrls: ['./version-overview.component.scss']
})
export class VersionOverviewComponent implements OnInit {
  versionsWithInterfaceAndApplication$: Observable<
    (Version | { interface: Interface } | { application: Application })[]
  >;

  constructor(private store: Store<AppState>, private router: Router) {}

  ngOnInit() {
    this.store.dispatch(loadInterfaces());
    this.store.dispatch(loadApplications());
    this.store.dispatch(loadVersions());

    this.versionsWithInterfaceAndApplication$ = this.store.select(
      selectVersionsWithInterfaceAndApplication
    );
  }

  onClickVersion(version: Version) {
    return this.router.navigate(['versions', version.id]);
  }
}
