import {fireEvent, render} from '@testing-library/angular';
import { environment } from '../../../environments/environment';
import { InterfaceUpdateComponent } from './interface-update.component';
import { Application } from '../../models/application';
import { InterfaceFormComponent } from '../interface-form/interface-form.component';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../material/material.module';
import { StoreModule } from '@ngrx/store';
import { interfaceReducer } from '../store/reducers/interface.reducers';
import { applicationReducer } from '../../application/store/reducers/application.reducers';
import { EffectsModule } from '@ngrx/effects';
import { InterfaceEffects } from '../store/effects/interface.effects';
import { ApplicationEffects } from '../../application/store/effects/application.effects';
import { ErrorEffects } from '../../store/effects/error.effects';
import { ReactiveFormsModule } from '@angular/forms';
import { createMock } from '@testing-library/angular/jest-utils';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Interface } from '../../models/interface';
import { when } from 'jest-when';
import { of, throwError } from 'rxjs';
import userEvent from "@testing-library/user-event";

describe('InterfaceUpdateComponent', () => {
  const applications: Application[] = [
    {
      id: 'applicationA',
      name: 'applicationA',
      description: 'applicationA',
      contact: 'applicationA'
    }
  ];

  const existingInterface: Interface = {
    id: '123',
    name: 'existingInterface',
    description: 'existingDescription',
    type: 'GRAPHQL',
    applicationId: 'applicationA'
  };

  const imports = [
    CommonModule,
    MaterialModule,
    StoreModule.forRoot({
      interfaceState: interfaceReducer,
      applicationState: applicationReducer
    }),
    EffectsModule.forRoot([InterfaceEffects, ApplicationEffects, ErrorEffects]),
    ReactiveFormsModule
  ];

  const declarations = [InterfaceFormComponent];

  const httpClientMock = createMock(HttpClient);
  const matDialogRefMock = createMock(MatDialogRef);
  const snackBarMock = createMock(MatSnackBar);

  const providers = [
    { provide: HttpClient, useValue: httpClientMock },
    {
      provide: MatDialogRef,
      useValue: matDialogRefMock
    },
    { provide: MatSnackBar, useValue: snackBarMock },
    {
      provide: MAT_DIALOG_DATA,
      useValue: { interface: existingInterface }
    }
  ];

  beforeAll(() => {
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/applications')
      .mockReturnValue(of(applications));
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/interfaces')
      .mockReturnValue(of([]));

    httpClientMock.put.mockReturnValue(of({}));
  });

  it('should update interface', async () => {
    // given
    const component = await render(InterfaceUpdateComponent, {
      imports,
      declarations,
      providers
    });

    const inputValues = {
      id: '123',
      name: 'My Interface',
      description: 'Interface description',
      type: 'REST',
      applicationId: 'applicationA'
    };

    const application = component.getByLabelText(/application/i) as any;
    const name = component.getByLabelText(/name/i) as any;
    const description = component.getByLabelText(/description/i) as any;
    const type = component.getByLabelText(/type/i) as any;

    const save = component.getByText(/save/i);

    // then
    expect(name.value).toBe('existingInterface');
    expect(description.value).toBe('existingDescription');

    // when
    userEvent.type(name, inputValues.name);
    userEvent.type(description, inputValues.description);

    userEvent.click(application);
    userEvent.selectOptions(application, 'applicationA');

    userEvent.click(type);
    userEvent.selectOptions(type, 'REST');

    save.click();

    // then
    expect(httpClientMock.put).toHaveBeenCalledWith(
      environment.apiUrl + '/interfaces/123',
      inputValues
    );
  });

  it('should close modal when form is cancelled', async () => {
    // given
    const component = await render(InterfaceUpdateComponent, {
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

    const component = await render(InterfaceUpdateComponent, {
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
      'Error updating interface. Please try again.',
      'close',
      { duration: 3000 }
    );
  });
});
