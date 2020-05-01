import { InterfaceOverviewComponent } from './interface-overview.component';
import { render } from '@testing-library/angular';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../material/material.module';
import { StoreModule } from '@ngrx/store';
import { interfaceReducer } from '../store/reducers/interface.reducers';
import { EffectsModule } from '@ngrx/effects';
import { InterfaceEffects } from '../store/effects/interface.effects';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { InterfaceTableComponent } from '../interface-table/interface-table.component';
import { Application } from '../../models/application';
import { Interface } from '../../models/interface';
import { createMock } from '@testing-library/angular/jest-utils';
import { HttpClient } from '@angular/common/http';
import { when } from 'jest-when';
import { environment } from '../../../environments/environment';
import { of } from 'rxjs';
import { applicationReducer } from '../../application/store/reducers/application.reducers';
import { ApplicationEffects } from '../../application/store/effects/application.effects';

describe('InterfaceOverviewComponent', () => {
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
    },
    {
      id: 'interfaceB',
      name: 'interfaceB',
      description: 'interfaceB',
      type: 'GRAPHQL',
      applicationId: 'applicationA'
    }
  ];

  const httpClientMock = createMock(HttpClient);

  const imports = [
    CommonModule,
    MaterialModule,
    StoreModule.forRoot({
      interfaceState: interfaceReducer,
      applicationState: applicationReducer
    }),
    EffectsModule.forRoot([InterfaceEffects, ApplicationEffects]),
    ReactiveFormsModule,
    RouterTestingModule,
    HttpClientTestingModule
  ];

  const declarations = [InterfaceTableComponent];

  const providers = [
    {
      provide: HttpClient,
      useValue: httpClientMock
    }
  ];

  it('should show list of interfaces', async () => {
    // given
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/applications')
      .mockReturnValue(of(applications));
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/interfaces')
      .mockReturnValue(of(interfaces));

    // when
    const component = await render(InterfaceOverviewComponent, {
      imports,
      declarations,
      providers
    });
    await component.fixture.whenStable();
    component.detectChanges();

    // then
    expect(component.getByRole('grid')).toBeDefined();

    expect(component.getAllByText(applications[0].name)).toBeDefined();

    expect(component.getByText(interfaces[0].name)).toBeDefined();
    expect(component.getByText(interfaces[1].name)).toBeDefined();

    expect(component.getByText(interfaces[0].type)).toBeDefined();
    expect(component.getByText(interfaces[1].type)).toBeDefined();
  });

  it('should display a hint when no interfaces are available', async () => {
    // given
    httpClientMock.get.mockReturnValue(of([]));

    // when
    const component = await render(InterfaceOverviewComponent, {
      imports,
      providers,
      declarations
    });
    await component.fixture.whenStable();
    component.fixture.detectChanges();

    // then
    expect(
      component.getByText('There are no interfaces available.')
    ).toBeDefined();
    expect(component.queryByRole('grid')).toBeNull();
    expect(component.queryByPlaceholderText('Filter')).toBeNull();
  });
});
