import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Application } from '../../models/application';
import { ApplicationService } from '../../services/application.service';

@Component({
  selector: 'app-application-create',
  templateUrl: './application-create.component.html',
  styleUrls: ['./application-create.component.scss']
})
export class ApplicationCreateComponent implements OnInit {
  constructor(
    private dialogRef: MatDialogRef<ApplicationCreateComponent>,
    private applicationService: ApplicationService
  ) {}

  ngOnInit() {}

  onCancel() {
    this.dialogRef.close();
  }

  async onSave(application: Application) {
    await this.applicationService.createApplication(application);
    this.dialogRef.close();
  }
}
