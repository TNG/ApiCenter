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
import { createMock } from '@testing-library/angular/jest-utils';

describe('ApplicationDeleteComponent', () => {
  const testApplication: Application = {
    name: 'Application 1',
    description: 'an application',
    contact: 'Max Mustermann',
    id: '123'
  };

  const matDialogRefMock = createMock(MatDialogRef);
  const httpClientMock = createMock(HttpClient);
  const routerMock = createMock(Router);

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

  beforeEach(() => {
    httpClientMock.delete.mockReset();
    httpClientMock.delete.mockReturnValue(of(EMPTY));
  });

  it('should delete an application when confirmed and route to application overview', async () => {
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
    expect(routerMock.navigate).toHaveBeenCalledWith(['applications']);
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
