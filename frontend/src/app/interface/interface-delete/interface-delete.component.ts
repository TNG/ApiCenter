import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { Router } from '@angular/router';
import { Interface } from '../../models/interface';
import { AppState } from '../../store/state/app.state';
import { deleteInterface } from '../store/actions/interface.actions';

@Component({
  selector: 'app-interface-delete',
  templateUrl: './interface-delete.component.html',
  styleUrls: ['./interface-delete.component.scss']
})
export class InterfaceDeleteComponent implements OnInit {
  interface: Interface;

  constructor(
    private dialogRef: MatDialogRef<InterfaceDeleteComponent>,
    private store: Store<AppState>,
    @Inject(MAT_DIALOG_DATA) data: { interface: Interface },
    private router: Router
  ) {
    this.interface = data.interface;
  }

  ngOnInit() {}

  onCancel() {
    this.dialogRef.close();
  }

  onConfirm() {
    this.store.dispatch(deleteInterface({ interface: this.interface }));
    this.dialogRef.close();

    return this.router.navigate(['interfaces']);
  }
}
