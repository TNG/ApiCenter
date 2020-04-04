import { Application } from '../../models/application';
import { ApplicationDeleteComponent } from './application-delete.component';
import { render } from '@testing-library/angular';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MaterialModule } from '../../material/material.module';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { EMPTY, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ApplicationEffects } from '../store/effects/application.effects';
import { Router } from '@angular/router';

describe('ApplicationDeleteComponent', () => {
  const testApplication: Application = {
    name: 'Application 1',
    description: 'an application',
    contact: 'Max Mustermann',
    id: '123'
  };

  const matDialogRefMock = jasmine.createSpyObj('MatDialogRef', ['close']);
  const httpClientMock = jasmine.createSpyObj('HttpClient', {
    delete: of(EMPTY),
    get: of([])
  });
  const routerMock = jasmine.createSpyObj('Router', ['navigate']);

  const providers = [
    { provide: MatDialogRef, useValue: matDialogRefMock },
    { provide: HttpClient, useValue: httpClientMock },
    {
      provide: MAT_DIALOG_DATA,
      useValue: { application: testApplication }
    },
    { provide: Router, useValue: routerMock }
  ];

  const imports = [
    MaterialModule,
    StoreModule.forRoot({}),
    EffectsModule.forRoot([ApplicationEffects])
  ];

  afterEach(() => {
    matDialogRefMock.close.calls.reset();
    httpClientMock.delete.calls.reset();
    routerMock.navigate.calls.reset();
  });

  it('should delete an application when confirmed and route to home', async () => {
    // given
    const component = await render(ApplicationDeleteComponent, {
      imports,
      providers
    });

    // when
    const confirmationButton = component.getByText('Delete');
    confirmationButton.click();

    // then
    expect(httpClientMock.delete).toHaveBeenCalledWith(
      environment.apiUrl + '/applications/123'
    );
    expect(matDialogRefMock.close).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });

  it('should close dialog when deletion is canceled', async () => {
    // given
    const component = await render(ApplicationDeleteComponent, {
      imports,
      providers
    });

    // when
    const cancellationButton = component.getByText('Cancel');
    cancellationButton.click();

    // then
    await expect(httpClientMock.delete).not.toHaveBeenCalledWith(
      environment.apiUrl + '/applications/123'
    );
    expect(matDialogRefMock.close).toHaveBeenCalled();
  });
});
