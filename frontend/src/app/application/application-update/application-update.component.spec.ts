import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material/material.module';
import { ApplicationModule } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { ApplicationEffects } from '../store/effects/application.effects';
import { ErrorEffects } from '../../store/effects/error.effects';
import { ApplicationFormComponent } from '../application-form/application-form.component';
import { throwError } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { render } from '@testing-library/angular';
import { Application } from '../../models/application';
import { ApplicationUpdateComponent } from './application-update.component';
import { environment } from '../../../environments/environment';
import { createMock } from '@testing-library/angular/jest-utils';
import userEvent from "@testing-library/user-event";

describe('ApplicationUpdateComponent', () => {
  const existingApplication: Application = {
    id: 'existingId',
    name: 'existingName',
    description: 'existingDescription',
    contact: 'existingContact',
    interfaceIds: []
  };

  const imports = [
    ReactiveFormsModule,
    MaterialModule,
    ApplicationModule,
    StoreModule.forRoot({}),
    EffectsModule.forRoot([ApplicationEffects, ErrorEffects])
  ];
  const declarations = [ApplicationFormComponent];

  const matDialogRefMock = createMock(MatDialogRef);
  const httpClientMock = createMock(HttpClient);
  const snackBarMock = createMock(MatSnackBar);

  const providers = [
    { provide: MatDialogRef, useValue: matDialogRefMock },
    { provide: HttpClient, useValue: httpClientMock },
    { provide: MatSnackBar, useValue: snackBarMock },
    {
      provide: MAT_DIALOG_DATA,
      useValue: { application: existingApplication }
    }
  ];

  it('should update application', async () => {
    // given
    const component = await render(ApplicationUpdateComponent, {
      imports,
      declarations,
      providers
    });

    const inputValues = {
      id: 'existingId',
      name: 'My application',
      description: 'Application description',
      contact: 'My contact',
      interfaceIds: []
    };

    const name = component.getByLabelText(/name/i) as any;
    const description = component.getByLabelText(/description/i) as any;
    const contact = component.getByLabelText(/contact/i) as any;

    const save = component.getByText(/save/i);

    // then
    expect(name.value).toBe('existingName');
    expect(description.value).toBe('existingDescription');
    expect(contact.value).toBe('existingContact');

    // when
    userEvent.type(name, inputValues.name);
    userEvent.type(description, inputValues.description);
    userEvent.type(contact, inputValues.contact);

    save.click();

    // then
    expect(httpClientMock.put).toHaveBeenCalledWith(
      environment.apiUrl + '/applications/existingId',
      inputValues
    );
  });

  it('should close modal when form is cancelled', async () => {
    // given
    const component = await render(ApplicationUpdateComponent, {
      imports,
      declarations,
      providers
    });

    const cancel = component.getByText(/cancel/i);

    // when
    cancel.click();

    // then
    expect(matDialogRefMock.close).toHaveBeenCalled();
  });

  it('should show snackbar if backend responds with error', async () => {
    // given
    httpClientMock.put.mockReturnValue(throwError(new HttpErrorResponse({})));

    const component = await render(ApplicationUpdateComponent, {
      imports,
      declarations,
      providers
    });

    const save = component.getByText(/save/i);

    // when
    save.click();

    // then
    expect(
      snackBarMock.open
    ).toHaveBeenCalledWith(
      'Error updating application. Please try again.',
      'close',
      { duration: 3000 }
    );
  });
});
