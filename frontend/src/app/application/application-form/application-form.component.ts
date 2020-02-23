import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Application } from '../../models/application';

@Component({
  selector: 'app-application-form',
  templateUrl: './application-form.component.html',
  styleUrls: ['./application-form.component.scss']
})
export class ApplicationFormComponent implements OnInit {
  @Output() save = new EventEmitter<Application>();
  @Output() cancel = new EventEmitter<void>();

  applicationForm: FormGroup;

  constructor() {}

  ngOnInit() {
    this.applicationForm = new FormGroup({
      name: new FormControl('', [Validators.required]),
      description: new FormControl('', [Validators.required]),
      contact: new FormControl('', [Validators.required])
    });
  }

  onCancel() {
    this.cancel.emit();
  }

  onSubmit(formValue) {
    const application: Application = {
      name: formValue.name,
      description: formValue.description,
      contact: formValue.contact
    };

    this.save.emit(application);
  }
}
