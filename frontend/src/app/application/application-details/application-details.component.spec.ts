import { ApplicationDetailsComponent } from './application-details.component';
import { render } from '@testing-library/angular';
import { MaterialModule } from '../../material/material.module';
import { RouterTestingModule } from '@angular/router/testing';
import { StoreModule } from '@ngrx/store';
import { applicationReducer } from '../store/reducers/application.reducers';
import { EffectsModule } from '@ngrx/effects';
import { ApplicationEffects } from '../store/effects/application.effects';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { ApplicationInterfaceOverviewComponent } from './application-interface-overview/application-interface-overview.component';
import { ApplicationInterfaceTableComponent } from './application-interface-overview/application-interface-table/application-interface-table.component';
import { createMock } from '@testing-library/angular/jest-utils';

describe('ApplicationDetailsComponent', () => {
  const application = {
    name: 'Application1',
    description: 'anApplication',
    contact: 'aContact',
    id: '123'
  };

  const httpClientMock = createMock(HttpClient);

  const imports = [
    MaterialModule,
    RouterTestingModule,
    StoreModule.forRoot({ applicationState: applicationReducer }),
    EffectsModule.forRoot([ApplicationEffects])
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

  const declarations = [
    ApplicationInterfaceOverviewComponent,
    ApplicationInterfaceTableComponent
  ];

  beforeEach(() => {
    httpClientMock.get.mockReturnValue(of(application));
  });

  it('should show details of applications', async () => {
    // when
    const component = await render(ApplicationDetailsComponent, {
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
