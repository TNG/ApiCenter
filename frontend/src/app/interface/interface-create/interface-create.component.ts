import { Component, OnInit } from '@angular/core';
import { Application } from '../../models/application';
import { Observable } from 'rxjs';
import { AppState } from '../../store/state/app.state';
import { Store } from '@ngrx/store';
import { selectApplications } from '../../application/store/selectors/application.selectors';
import { Interface } from '../../models/interface';
import { MatDialogRef } from '@angular/material/dialog';
import { createInterface } from '../store/actions/interface.actions';
import { loadApplications } from '../../application/store/actions/application.actions';

@Component({
  selector: 'app-interface-create',
  templateUrl: './interface-create.component.html',
  styleUrls: ['./interface-create.component.scss']
})
export class InterfaceCreateComponent implements OnInit {
  applications$: Observable<Application[]>;

  constructor(
    private store: Store<AppState>,
    private dialogRef: MatDialogRef<InterfaceCreateComponent>
  ) {}

  ngOnInit() {
    this.store.dispatch(loadApplications());

    this.applications$ = this.store.select(selectApplications);
  }

  onSave(myInterface: Interface) {
    this.store.dispatch(createInterface({ interface: myInterface }));
    this.dialogRef.close();
  }

  onCancel() {
    this.dialogRef.close();
  }
}
