import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { Application } from '../../models/application';
import { updateApplication } from '../store/actions/application.actions';

@Component({
  selector: 'app-application-update',
  templateUrl: './application-update.component.html',
  styleUrls: ['./application-update.component.scss']
})
export class ApplicationUpdateComponent implements OnInit {
  application: Application;

  constructor(
    private dialogRef: MatDialogRef<ApplicationUpdateComponent>,
    private store: Store<Application>,
    @Inject(MAT_DIALOG_DATA) data: { application: Application }
  ) {
    this.application = data.application;
  }

  ngOnInit() {}

  onCancel() {
    this.dialogRef.close();
  }

  onSave(application: Application) {
    this.store.dispatch(updateApplication({ application }));
    this.dialogRef.close();
  }
}
