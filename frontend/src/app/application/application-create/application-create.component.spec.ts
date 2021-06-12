import { render, RenderResult } from '@testing-library/angular';
import { ApplicationCreateComponent } from './application-create.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material/material.module';
import { ApplicationModule } from '@angular/core';
import { ApplicationFormComponent } from '../application-form/application-form.component';
import { MatDialogRef } from '@angular/material/dialog';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Application } from '../../models/application';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { ApplicationEffects } from '../store/effects/application.effects';
import { ErrorEffects } from '../../store/effects/error.effects';
import { createMock } from '@testing-library/angular/jest-utils';
import userEvent from "@testing-library/user-event";

describe('ApplicationCreateComponent', () => {
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
    { provide: MatSnackBar, useValue: snackBarMock }
  ];

  beforeEach(() => {
    httpClientMock.post.mockReturnValue(of({}));
    httpClientMock.get.mockReturnValue(of([]));
  });

  it('should save new application', async () => {
    // given
    const component = await render(ApplicationCreateComponent, {
      imports,
      declarations,
      providers
    });

    const inputValues = {
      id: undefined,
      name: 'My application',
      description: 'Application description',
      contact: 'My contact',
      interfaceIds: []
    };

    const save = component.getByText(/save/i);

    // when
    await fillApplicationForm(component, inputValues);
    save.click();

    // then
    expect(httpClientMock.post).toHaveBeenCalledWith(
      'http://localhost:8080/api/applications',
      inputValues
    );
  });

  it('should close modal when form is cancelled', async () => {
    // given
    const component = await render(ApplicationCreateComponent, {
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
    httpClientMock.post.mockReturnValue(throwError(new HttpErrorResponse({})));

    const component = await render(ApplicationCreateComponent, {
      imports,
      declarations,
      providers
    });

    const inputValues = {
      id: undefined,
      name: 'My application',
      description: 'Application description',
      contact: 'My contact'
    };

    const save = component.getByText(/save/i);

    // when
    await fillApplicationForm(component, inputValues);
    save.click();

    // then
    expect(
      snackBarMock.open
    ).toHaveBeenCalledWith(
      'Error creating application. Please try again.',
      'close',
      { duration: 3000 }
    );
  });

  async function fillApplicationForm(
    component: RenderResult<ApplicationCreateComponent>,
    inputValues: Application
  ) {
    const name = component.getByLabelText(/name/i);
    const description = component.getByLabelText(/description/i);
    const contact = component.getByLabelText(/contact/i);

    userEvent.type(name, inputValues.name);
    userEvent.type(description, inputValues.description);
    userEvent.type(contact, inputValues.contact);
  }
});
