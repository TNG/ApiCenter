import { Component, OnInit } from '@angular/core';
import { Interface } from '../../models/interface';
import { AppState } from '../../store/state/app.state';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { selectInterfacesWithApplications } from '../selectors/interface.selectors';
import { loadInterfaces } from '../store/actions/interface.actions';
import { loadApplications } from '../../application/store/actions/application.actions';

@Component({
  selector: 'app-interface-overview',
  templateUrl: './interface-overview.component.html',
  styleUrls: ['./interface-overview.component.scss']
})
export class InterfaceOverviewComponent implements OnInit {
  interfacesWithApplication$: Observable<
    (Interface | { application: string })[]
  >;

  constructor(private store: Store<AppState>) {}

  ngOnInit() {
    this.store.dispatch(loadInterfaces());
    this.store.dispatch(loadApplications());

    this.interfacesWithApplication$ = this.store.select(
      selectInterfacesWithApplications
    );
  }
}
