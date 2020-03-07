import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Application } from '../../models/application';
import { Store } from '@ngrx/store';
import { createApplication } from '../store/actions/application.actions';

@Component({
  selector: 'app-application-create',
  templateUrl: './application-create.component.html',
  styleUrls: ['./application-create.component.scss']
})
export class ApplicationCreateComponent implements OnInit {
  constructor(
    private dialogRef: MatDialogRef<ApplicationCreateComponent>,
    private store: Store<Application>
  ) {}

  ngOnInit() {}

  onCancel() {
    this.dialogRef.close();
  }

  async onSave(application: Application) {
    this.store.dispatch(createApplication({ application }));
    this.dialogRef.close();
  }
}
