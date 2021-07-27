import { InterfaceCreateComponent } from './interface-create.component';
import { render, RenderResult } from '@testing-library/angular';
import { InterfaceFormComponent } from '../interface-form/interface-form.component';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../material/material.module';
import { StoreModule } from '@ngrx/store';
import { interfaceReducer } from '../store/reducers/interface.reducers';
import { EffectsModule } from '@ngrx/effects';
import { InterfaceEffects } from '../store/effects/interface.effects';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { createMock } from '@testing-library/angular/jest-utils';
import { MatDialogRef } from '@angular/material/dialog';
import { Interface } from '../../models/interface';
import { Application } from '../../models/application';
import { environment } from '../../../environments/environment';
import { when } from 'jest-when';
import { of, throwError } from 'rxjs';
import { applicationReducer } from '../../application/store/reducers/application.reducers';
import { ApplicationEffects } from '../../application/store/effects/application.effects';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ErrorEffects } from '../../store/effects/error.effects';
import userEvent from "@testing-library/user-event";

describe('InterfaceCreateComponent', () => {
  const applications: Application[] = [
    {
      id: 'applicationA',
      name: 'applicationA',
      description: 'applicationA',
      contact: 'applicationA'
    }
  ];

  const declarations = [InterfaceFormComponent];

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

  const httpClientMock = createMock(HttpClient);
  const matDialogRefMock = createMock(MatDialogRef);
  const snackBarMock = createMock(MatSnackBar);

  const providers = [
    { provide: HttpClient, useValue: httpClientMock },
    {
      provide: MatDialogRef,
      useValue: matDialogRefMock
    },
    { provide: MatSnackBar, useValue: snackBarMock }
  ];

  beforeEach(() => {
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/applications')
      .mockReturnValue(of(applications));
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/interfaces')
      .mockReturnValue(of([]));
  });

  it('should create an interface', async () => {
    // given
    const newInterface: Interface = {
      name: 'testInterface',
      description: 'testDescription',
      type: 'GRAPHQL',
      applicationId: 'applicationA'
    };
    httpClientMock.post.mockReturnValue(of(newInterface));

    const component = await render(InterfaceCreateComponent, {
      declarations,
      imports,
      providers
    });

    const inputValues: Interface & { applicationName: string } = {
      name: newInterface.name,
      description: newInterface.description,
      type: newInterface.type,
      applicationName: 'applicationA',
      applicationId: undefined
    };
    await component.fixture.whenStable();
    component.fixture.detectChanges();

    const save = component.getByText(/save/i);

    // when
    await fillInterfaceForm(component, inputValues);
    save.click();

    // then
    expect(httpClientMock.post).toHaveBeenCalledWith(
      environment.apiUrl + '/interfaces',
      newInterface
    );
  });

  it('should show snackbar if backend responds with error', async () => {
    // given
    httpClientMock.post.mockReturnValue(throwError(new HttpErrorResponse({})));

    const component = await render(InterfaceCreateComponent, {
      imports,
      declarations,
      providers
    });

    const inputValues = {
      id: undefined,
      name: 'My interface',
      description: 'Interface description',
      type: 'REST',
      applicationId: undefined,
      applicationName: 'applicationA'
    };

    const save = component.getByText(/save/i);

    // when
    await fillInterfaceForm(component, inputValues);
    save.click();

    // then
    expect(
      snackBarMock.open
    ).toHaveBeenCalledWith(
      'Error creating interface. Please try again.',
      'close',
      { duration: 3000 }
    );
  });

  async function fillInterfaceForm(
    component: RenderResult<InterfaceCreateComponent>,
    inputValues: Interface & { applicationName: string }
  ) {
    const application = component.getByLabelText(/application/i);
    const name = component.getByLabelText(/name/i);
    const description = component.getByLabelText(/description/i);
    const type = component.getByLabelText(/type/i);

    userEvent.click(application);
    userEvent.selectOptions(application, inputValues.applicationName);
    userEvent.type(name, inputValues.name);
    userEvent.type(description, inputValues.description);
    userEvent.click(type);
    userEvent.selectOptions(type, inputValues.type);
    userEvent.click(type);
  }
});
