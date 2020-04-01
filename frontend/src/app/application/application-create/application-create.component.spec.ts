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

describe('ApplicationCreateComponent', () => {
  const imports = [
    ReactiveFormsModule,
    MaterialModule,
    ApplicationModule,
    StoreModule.forRoot({}),
    EffectsModule.forRoot([ApplicationEffects, ErrorEffects])
  ];
  const declarations = [ApplicationFormComponent];

  const matDialogRefMock = jasmine.createSpyObj('MatDialogRef', ['close']);
  const httpClientMock = jasmine.createSpyObj('HttpClient', { post: of({}) });
  const snackBarMock = jasmine.createSpyObj('MatSnackBar', ['open']);

  const providers = [
    { provide: MatDialogRef, useValue: matDialogRefMock },
    { provide: HttpClient, useValue: httpClientMock },
    { provide: MatSnackBar, useValue: snackBarMock }
  ];

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
      contact: 'My contact'
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
    const testHttpClientMock = jasmine.createSpyObj('HttpClient', {
      post: throwError(new HttpErrorResponse({}))
    });
    const testProviders = [
      ...providers,
      { provide: HttpClient, useValue: testHttpClientMock }
    ];

    const component = await render(ApplicationCreateComponent, {
      imports,
      declarations,
      providers: testProviders
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

    await component.type(name, inputValues.name);
    await component.type(description, inputValues.description);
    await component.type(contact, inputValues.contact);
  }
});
