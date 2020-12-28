import { Application } from '../../models/application';
import { Interface } from '../../models/interface';
import { createMock } from '@testing-library/angular/jest-utils';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../material/material.module';
import { StoreModule } from '@ngrx/store';
import { interfaceReducer } from '../../interface/store/reducers/interface.reducers';
import { applicationReducer } from '../../application/store/reducers/application.reducers';
import { EffectsModule } from '@ngrx/effects';
import { InterfaceEffects } from '../../interface/store/effects/interface.effects';
import { ApplicationEffects } from '../../application/store/effects/application.effects';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Version } from '../../models/version';
import { versionReducer } from '../store/reducers/version.reducers';
import { VersionEffects } from '../store/effects/version.effects';
import { environment } from '../../../environments/environment';
import { of } from 'rxjs';
import { render } from '@testing-library/angular';
import { when } from 'jest-when';
import { VersionOverviewComponent } from './version-overview.component';
import { VersionTableComponent } from '../version-table/version-table.component';

describe('VersionOverviewComponent', () => {
  const applications: Application[] = [
    {
      id: 'applicationA',
      name: 'applicationA',
      description: 'applicationA',
      contact: 'applicationA'
    }
  ];

  const interfaces: Interface[] = [
    {
      id: 'interfaceA',
      name: 'interfaceA',
      description: 'interfaceA',
      type: 'REST',
      applicationId: 'applicationA'
    }
  ];

  const versions: Version[] = [
    {
      id: 'versionA',
      title: 'versionA',
      version: '1.0',
      content: 'contentA',
      interfaceId: 'interfaceA'
    },
    {
      id: 'versionB',
      title: 'versionB',
      version: '1.0',
      content: 'contentB',
      interfaceId: 'interfaceA'
    }
  ];

  const httpClientMock = createMock(HttpClient);

  const imports = [
    CommonModule,
    MaterialModule,
    StoreModule.forRoot({
      interfaceState: interfaceReducer,
      applicationState: applicationReducer,
      versionState: versionReducer
    }),
    EffectsModule.forRoot([
      VersionEffects,
      InterfaceEffects,
      ApplicationEffects
    ]),
    ReactiveFormsModule,
    RouterTestingModule,
    HttpClientTestingModule
  ];

  const declarations = [VersionTableComponent];

  const providers = [
    {
      provide: HttpClient,
      useValue: httpClientMock
    }
  ];

  it('should show list of versions', async () => {
    // given
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/applications')
      .mockReturnValue(of(applications));
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/interfaces')
      .mockReturnValue(of(interfaces));
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/versions')
      .mockReturnValue(of(versions));

    // when
    const component = await render(VersionOverviewComponent, {
      imports,
      declarations,
      providers
    });
    await component.fixture.whenStable();
    component.detectChanges();

    // then
    expect(component.getByRole('grid')).toBeDefined();

    expect(component.getAllByText(applications[0].name)).toBeDefined();
    expect(component.getAllByText(interfaces[0].name)).toBeDefined();
    expect(component.getAllByText(interfaces[0].type)).toBeDefined();

    expect(
      component.getByText(`${versions[0].title} (${versions[0].version})`)
    ).toBeDefined();
    expect(
      component.getByText(`${versions[1].title} (${versions[1].version})`)
    ).toBeDefined();
  });

  it('should display a hint when no versions are available', async () => {
    // given
    httpClientMock.get.mockReturnValue(of([]));

    // when
    const component = await render(VersionOverviewComponent, {
      imports,
      providers,
      declarations
    });
    await component.fixture.whenStable();
    component.fixture.detectChanges();

    // then
    expect(
      component.getByText('There are no versions available.')
    ).toBeDefined();
    expect(component.queryByRole('grid')).toBeNull();
    expect(component.queryByPlaceholderText('Filter')).toBeNull();
  });
});
