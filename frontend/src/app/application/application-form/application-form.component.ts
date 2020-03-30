import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Application } from '../../models/application';

@Component({
  selector: 'app-application-form',
  templateUrl: './application-form.component.html',
  styleUrls: ['./application-form.component.scss']
})
export class ApplicationFormComponent implements OnInit {
  @Input() application: Application;
  @Output() save = new EventEmitter<Application>();
  @Output() cancel = new EventEmitter<void>();

  applicationForm: FormGroup;

  constructor() {}

  ngOnInit() {
    if (!this.application) {
      this.application = { name: '', description: '', contact: '' };
    }

    this.applicationForm = new FormGroup({
      name: new FormControl(this.application.name, [Validators.required]),
      description: new FormControl(this.application.description, [
        Validators.required
      ]),
      contact: new FormControl(this.application.contact, [Validators.required])
    });
  }

  onCancel() {
    this.cancel.emit();
  }

  onSubmit(formValue) {
    const application: Application = {
      id: this.application.id,
      name: formValue.name,
      description: formValue.description,
      contact: formValue.contact
    };

    this.save.emit(application);
  }
}
