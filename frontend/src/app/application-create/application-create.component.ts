import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-application-create',
  templateUrl: './application-create.component.html',
  styleUrls: ['./application-create.component.scss']
})
export class ApplicationCreateComponent implements OnInit {
  constructor(private dialogRef: MatDialogRef<ApplicationCreateComponent>) {}

  ngOnInit() {}

  onCancel() {
    this.dialogRef.close();
  }
}
