import { render } from '@testing-library/angular';
import { environment } from '../../../environments/environment';
import { InterfaceDeleteComponent } from './interface-delete.component';
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
import { HttpClient } from '@angular/common/http';
import { createMock } from '@testing-library/angular/jest-utils';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Interface } from '../../models/interface';
import { EMPTY, of } from 'rxjs';

describe('InterfaceDeleteComponent', () => {
  const testInterface: Interface = {
    id: '1234',
    name: 'Interface',
    description: 'Description',
    type: 'GRAPHQL',
    applicationId: 'appA'
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

  const httpClientMock = createMock(HttpClient);
  const matDialogRefMock = createMock(MatDialogRef);
  const routerMock = createMock(Router);

  const providers = [
    {
      provide: HttpClient,
      useValue: httpClientMock
    },
    { provide: MatDialogRef, useValue: matDialogRefMock },
    { provide: Router, useValue: routerMock },
    {
      provide: MAT_DIALOG_DATA,
      useValue: { interface: testInterface }
    }
  ];

  beforeAll(() => {
    httpClientMock.get.mockReturnValue(of([]));
  });

  beforeEach(() => {
    httpClientMock.delete.mockReset();
    httpClientMock.delete.mockReturnValue(of(EMPTY));
  });

  it('should delete an interface when confirmed and route to interface overview', async () => {
    // given
    const component = await render(InterfaceDeleteComponent, {
      imports,
      providers
    });

    // when
    const confirmationButton = component.getByText('Delete');
    confirmationButton.click();

    // then
    expect(httpClientMock.delete).toHaveBeenCalledWith(
      environment.apiUrl + '/interfaces/1234'
    );
    expect(matDialogRefMock.close).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['interfaces']);
  });

  it('should close dialog when deletion is canceled', async () => {
    // given
    const component = await render(InterfaceDeleteComponent, {
      imports,
      providers
    });

    // when
    const cancellationButton = component.getByText('Cancel');
    cancellationButton.click();

    // then
    await expect(httpClientMock.delete).not.toHaveBeenCalledWith(
      environment.apiUrl + '/interfaces/1234'
    );
    expect(matDialogRefMock.close).toHaveBeenCalled();
  });
});
