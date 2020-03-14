import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { Application } from '../../models/application';
import { deleteApplication } from '../store/actions/application.actions';

@Component({
  selector: 'app-application-delete',
  templateUrl: './application-delete.component.html',
  styleUrls: ['./application-delete.component.scss']
})
export class ApplicationDeleteComponent implements OnInit {
  application: Application;

  constructor(
    private dialogRef: MatDialogRef<ApplicationDeleteComponent>,
    private store: Store<Application>,
    @Inject(MAT_DIALOG_DATA) data: { application: Application }
  ) {
    this.application = data.application;
  }

  ngOnInit() {}

  onCancel() {
    this.dialogRef.close();
  }

  async onConfirm() {
    this.store.dispatch(deleteApplication({ application: this.application }));
    this.dialogRef.close();
  }
}
