import { Component, OnInit } from '@angular/core';
import { Interface } from '../../models/interface';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { loadApplication } from '../../application/store/actions/application.actions';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { selectApplication } from '../../application/store/selectors/application.selectors';
import { AppState } from '../../store/state/app.state';
import { Store } from '@ngrx/store';
import { loadInterface } from '../store/actions/interface.actions';
import { selectInterface } from '../selectors/interface.selectors';
import { Application } from '../../models/application';
import { MatDialog } from '@angular/material/dialog';
import { InterfaceUpdateComponent } from '../interface-update/interface-update.component';

@Component({
  selector: 'app-interface-details',
  templateUrl: './interface-details.component.html',
  styleUrls: ['./interface-details.component.scss']
})
export class InterfaceDetailsComponent implements OnInit {
  interface$: Observable<Interface>;
  application$: Observable<Application>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private store: Store<AppState>,
    private matDialog: MatDialog
  ) {}

  ngOnInit() {
    this.interface$ = this.activatedRoute.paramMap.pipe(
      tap(params => {
        this.store.dispatch(loadInterface({ id: params.get('id') }));
      }),
      switchMap((params: ParamMap) =>
        this.store.select(selectInterface, { id: params.get('id') })
      )
    );

    this.application$ = this.interface$.pipe(
      tap(myInterface => {
        this.store.dispatch(loadApplication({ id: myInterface.applicationId }));
      }),
      switchMap(myInterface =>
        this.store.select(selectApplication, { id: myInterface.applicationId })
      )
    );
  }

  onUpdateInterface(myInterface: Interface) {
    this.matDialog.open(InterfaceUpdateComponent, {
      data: { interface: myInterface }
    });
  }

  onDeleteInterface(myInterface: Interface) {}
}
