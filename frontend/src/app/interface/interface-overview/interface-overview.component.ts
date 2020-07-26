import { Component, OnInit } from '@angular/core';
import { Interface } from '../../models/interface';
import { AppState } from '../../store/state/app.state';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { selectInterfacesWithApplications } from '../store/selectors/interface.selectors';
import { loadInterfaces } from '../store/actions/interface.actions';
import { loadApplications } from '../../application/store/actions/application.actions';
import { MatDialog } from '@angular/material/dialog';
import { InterfaceCreateComponent } from '../interface-create/interface-create.component';
import { Application } from '../../models/application';
import { Router } from '@angular/router';

@Component({
  selector: 'app-interface-overview',
  templateUrl: './interface-overview.component.html',
  styleUrls: ['./interface-overview.component.scss']
})
export class InterfaceOverviewComponent implements OnInit {
  interfacesWithApplication$: Observable<
    (Interface | { application: Application })[]
  >;

  constructor(
    private store: Store<AppState>,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit() {
    this.store.dispatch(loadInterfaces());
    this.store.dispatch(loadApplications());

    this.interfacesWithApplication$ = this.store.select(
      selectInterfacesWithApplications
    );
  }

  onCreateInterface() {
    this.dialog.open(InterfaceCreateComponent);
  }

  onClickInterface(myInterface: Interface) {
    return this.router.navigate(['interfaces', myInterface.id]);
  }
}
