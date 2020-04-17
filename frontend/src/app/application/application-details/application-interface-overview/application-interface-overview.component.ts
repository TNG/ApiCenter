import { Component, Input, OnInit } from '@angular/core';
import { Application } from '../../../models/application';
import { Interface } from '../../../models/interface';
import { Observable } from 'rxjs';
import { AppState } from '../../../store/state/app.state';
import { Store } from '@ngrx/store';
import { selectInterfacesById } from '../../../interface/selectors/interface.selectors';
import { loadInterfaces } from '../../../interface/store/actions/interface.actions';
import { Router } from '@angular/router';

@Component({
  selector: 'app-application-interface-overview',
  templateUrl: './application-interface-overview.component.html',
  styleUrls: ['./application-interface-overview.component.scss']
})
export class ApplicationInterfaceOverviewComponent implements OnInit {
  @Input() application: Application;

  interfaces$: Observable<Interface[]>;

  constructor(private store: Store<AppState>, private router: Router) {}

  ngOnInit() {
    this.store.dispatch(loadInterfaces());

    this.interfaces$ = this.store.select(selectInterfacesById, {
      interfaceIds: this.application.interfaceIds
    });
  }

  onClickInterface(myInterface: Interface) {
    this.router.navigate(['interfaces', myInterface.id]);
  }
}
