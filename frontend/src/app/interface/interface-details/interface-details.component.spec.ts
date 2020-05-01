import { createMock } from '@testing-library/angular/jest-utils';
import { HttpClient } from '@angular/common/http';
import { MaterialModule } from '../../material/material.module';
import { StoreModule } from '@ngrx/store';
import { applicationReducer } from '../../application/store/reducers/application.reducers';
import { EffectsModule } from '@ngrx/effects';
import { ApplicationEffects } from '../../application/store/effects/application.effects';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';
import { render } from '@testing-library/angular';
import { Interface } from '../../models/interface';
import { CommonModule } from '@angular/common';
import { interfaceReducer } from '../store/reducers/interface.reducers';
import { InterfaceEffects } from '../store/effects/interface.effects';
import { ErrorEffects } from '../../store/effects/error.effects';
import { ReactiveFormsModule } from '@angular/forms';
import { InterfaceDetailsComponent } from './interface-details.component';
import { Application } from '../../models/application';
import { when } from 'jest-when';
import { environment } from '../../../environments/environment';

describe('InterfaceDetailsComponent', () => {
  const testInterface: Interface = {
    id: '123',
    name: 'Interface1',
    description: 'anInterface',
    type: 'REST',
    applicationId: 'applicationId'
  };

  const application: Application = {
    id: 'applicationId',
    name: 'applicationName',
    description: 'applicationDescription',
    contact: 'applicationContact'
  };

  const httpClientMock = createMock(HttpClient);

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

  const providers = [
    {
      provide: HttpClient,
      useValue: httpClientMock
    },
    {
      provide: ActivatedRoute,
      useValue: {
        paramMap: of(convertToParamMap({ id: '123' }))
      }
    }
  ];

  const declarations = [InterfaceDetailsComponent];

  beforeEach(() => {
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/interfaces/123')
      .mockReturnValue(of(testInterface));
    when(httpClientMock.get)
      .calledWith(environment.apiUrl + '/applications/applicationId')
      .mockReturnValue(of(application));
  });

  it('should show details of interfaces', async () => {
    // when
    const component = await render(InterfaceDetailsComponent, {
      imports,
      providers,
      declarations
    });

    // then
    expect(component.getByText(testInterface.name)).toBeDefined();
    expect(component.getByText(testInterface.description)).toBeDefined();
    expect(component.getByText(testInterface.type)).toBeDefined();
  });

  it('should show application details on interface details page', async () => {
    // when
    const component = await render(InterfaceDetailsComponent, {
      imports,
      providers,
      declarations
    });

    // then
    expect(component.getByText(application.name)).toBeDefined();
    expect(component.getByText(application.description)).toBeDefined();
    expect(component.getByText(application.contact)).toBeDefined();
  });
});
