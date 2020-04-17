import { Component, Inject, OnInit } from '@angular/core';
import { Application } from '../../models/application';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { AppState } from '../../store/state/app.state';
import { Interface } from '../../models/interface';
import { selectApplications } from '../../application/store/selectors/application.selectors';
import { Observable } from 'rxjs';
import { loadApplications } from '../../application/store/actions/application.actions';
import { updateInterface } from '../store/actions/interface.actions';

@Component({
  selector: 'app-interface-update',
  templateUrl: './interface-update.component.html',
  styleUrls: ['./interface-update.component.scss']
})
export class InterfaceUpdateComponent implements OnInit {
  interface: Interface;
  applications$: Observable<Application[]>;

  constructor(
    private dialogRef: MatDialogRef<InterfaceUpdateComponent>,
    private store: Store<AppState>,
    @Inject(MAT_DIALOG_DATA) data: { interface: Interface }
  ) {
    this.interface = data.interface;
  }

  ngOnInit(): void {
    this.store.dispatch(loadApplications());

    this.applications$ = this.store.select(selectApplications);
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSave(myInterface: Interface) {
    this.store.dispatch(updateInterface({ interface: myInterface }));

    this.dialogRef.close();
  }
}
