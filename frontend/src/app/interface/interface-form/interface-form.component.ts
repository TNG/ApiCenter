import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Application } from '../../models/application';
import { InterfaceType } from '../../models/interfaceType';
import { Interface } from '../../models/interface';

@Component({
  selector: 'app-interface-form',
  templateUrl: './interface-form.component.html',
  styleUrls: ['./interface-form.component.scss']
})
export class InterfaceFormComponent implements OnInit {
  @Input() interface: Interface;
  @Input() applications: Application[];
  @Output() save = new EventEmitter<Interface>();
  @Output() cancel = new EventEmitter<void>();

  interfaceTypes = Object.values(InterfaceType);

  interfaceForm: FormGroup;

  constructor() {}

  ngOnInit() {
    if (!this.interface) {
      this.interface = {
        name: '',
        description: '',
        type: null,
        applicationId: ''
      };
    }

    this.interfaceForm = new FormGroup({
      applicationId: new FormControl(this.interface.applicationId, [
        Validators.required
      ]),
      name: new FormControl(this.interface.name, [Validators.required]),
      description: new FormControl(this.interface.description),
      type: new FormControl(this.interface.type, [Validators.required])
    });
  }

  onSubmit(value) {
    const myInterface: Interface = {
      id: this.interface.id,
      name: value.name,
      description: value.description,
      type: value.type.toUpperCase(),
      applicationId: value.applicationId
    };

    this.save.emit(myInterface);
  }

  onCancel() {
    this.cancel.emit();
  }
}
