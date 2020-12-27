import { render } from '@testing-library/angular';
import { environment } from '../../../environments/environment';
import { VersionDeleteComponent } from './version-delete.component';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../material/material.module';
import { StoreModule } from '@ngrx/store';
import { applicationReducer } from '../../application/store/reducers/application.reducers';
import { EffectsModule } from '@ngrx/effects';
import { ApplicationEffects } from '../../application/store/effects/application.effects';
import { ErrorEffects } from '../../store/effects/error.effects';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { createMock } from '@testing-library/angular/jest-utils';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { EMPTY, of } from 'rxjs';
import { Version } from '../../models/version';
import { interfaceReducer } from '../../interface/store/reducers/interface.reducers';
import { versionReducer } from '../store/reducers/version.reducers';
import { InterfaceEffects } from '../../interface/store/effects/interface.effects';
import { VersionEffects } from '../store/effects/version.effects';

describe('VersionDeleteComponent', () => {
  const testVersion: Version = {
    id: 'versionId',
    title: 'versionTitle',
    version: 'v1',
    description: 'This is a version',
    content: 'Content of the version',
    interfaceId: 'interfaceId'
  };

  const imports = [
    CommonModule,
    MaterialModule,
    StoreModule.forRoot({
      versionState: versionReducer,
      interfaceState: interfaceReducer,
      applicationState: applicationReducer
    }),
    EffectsModule.forRoot([
      VersionEffects,
      InterfaceEffects,
      ApplicationEffects,
      ErrorEffects
    ]),
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
      useValue: { version: testVersion }
    }
  ];

  beforeAll(() => {
    httpClientMock.get.mockReturnValue(of([]));
  });

  beforeEach(() => {
    httpClientMock.delete.mockReset();
    httpClientMock.delete.mockReturnValue(of(EMPTY));
  });

  it('should delete a version when confirmed and route to versions overview', async () => {
    // given
    const component = await render(VersionDeleteComponent, {
      imports,
      providers
    });

    // when
    const confirmationButton = component.getByText('Delete');
    confirmationButton.click();

    // then
    expect(httpClientMock.delete).toHaveBeenCalledWith(
      environment.apiUrl + '/versions/versionId'
    );
    expect(matDialogRefMock.close).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['versions']);
  });

  it('should close dialog when deletion is canceled', async () => {
    // given
    const component = await render(VersionDeleteComponent, {
      imports,
      providers
    });

    // when
    const cancellationButton = component.getByText('Cancel');
    cancellationButton.click();

    // then
    await expect(httpClientMock.delete).not.toHaveBeenCalledWith(
      environment.apiUrl + '/versions/versionId'
    );
    expect(matDialogRefMock.close).toHaveBeenCalled();
  });
});
