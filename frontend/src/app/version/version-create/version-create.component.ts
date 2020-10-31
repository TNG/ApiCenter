import { Component, OnInit } from '@angular/core';
import { Interface } from '../../models/interface';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AppState } from '../../store/state/app.state';
import { Store } from '@ngrx/store';
import { loadInterfaces } from '../../interface/store/actions/interface.actions';
import { selectInterfacesWithApplications } from '../../interface/store/selectors/interface.selectors';
import { Observable } from 'rxjs';
import { loadApplications } from '../../application/store/actions/application.actions';
import { Application } from '../../models/application';
import { MatDialogRef } from '@angular/material/dialog';
import { createVersion } from '../store/actions/version.actions';

@Component({
  selector: 'app-version-create',
  templateUrl: './version-create.component.html',
  styleUrls: ['./version-create.component.scss']
})
export class VersionCreateComponent implements OnInit {
  interfaces$: Observable<(Interface | { application: Application })[]>;
  versionForm: FormGroup;
  versionFile: File;

  constructor(
    private store: Store<AppState>,
    private dialogRef: MatDialogRef<VersionCreateComponent>
  ) {}

  ngOnInit() {
    this.store.dispatch(loadInterfaces());
    this.store.dispatch(loadApplications());

    this.interfaces$ = this.store.select(selectInterfacesWithApplications);

    this.versionForm = new FormGroup({
      interfaceId: new FormControl('', [Validators.required]),
      versionFile: new FormControl('', [Validators.required])
    });
  }

  onFileChange(event) {
    this.versionFile = event.target.files[0];
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSubmit(value) {
    const fileReader = new FileReader();
    fileReader.onload = e => {
      const fileContent = fileReader.result as string;

      const interfaceId = value.interfaceId;

      this.store.dispatch(
        createVersion({ versionFile: { interfaceId, fileContent } })
      );
      this.dialogRef.close();
    };
    fileReader.readAsText(this.versionFile);
  }
}
