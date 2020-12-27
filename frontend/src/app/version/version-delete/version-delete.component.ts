import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { Router } from '@angular/router';
import { Interface } from '../../models/interface';
import { AppState } from '../../store/state/app.state';
import { deleteVersion } from '../store/actions/version.actions';
import { Version } from '../../models/version';

@Component({
  selector: 'app-version-delete',
  templateUrl: './version-delete.component.html',
  styleUrls: ['./version-delete.component.scss']
})
export class VersionDeleteComponent implements OnInit {
  version: Version;

  constructor(
    private dialogRef: MatDialogRef<VersionDeleteComponent>,
    private store: Store<AppState>,
    @Inject(MAT_DIALOG_DATA) data: { version: Version },
    private router: Router
  ) {
    this.version = data.version;
  }

  ngOnInit() {}

  onCancel() {
    this.dialogRef.close();
  }

  onConfirm() {
    this.store.dispatch(deleteVersion({ version: this.version }));
    this.dialogRef.close();

    return this.router.navigate(['versions']);
  }
}
