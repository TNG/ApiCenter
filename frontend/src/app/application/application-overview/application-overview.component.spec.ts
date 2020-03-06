import { ApplicationOverviewComponent } from './application-overview.component';
import { render } from '@testing-library/angular';
import { MaterialModule } from '../../material/material.module';
import { ApplicationModule } from '@angular/core';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Application } from '../../models/application';
import { environment } from '../../../environments/environment';

describe('ApplicationOverviewComponent', () => {
  const testApplications: Application[] = [
    {
      name: 'Application 1',
      description: 'an application',
      contact: 'Max Mustermann',
      id: '123'
    },
    {
      name: 'Application 2',
      description: 'another application',
      contact: 'Erika Mustermann',
      id: '1234'
    },
    {
      name: 'Application 3',
      description: 'yet another application',
      contact: 'Horst Mustermann',
      id: '12345'
    }
  ];
  const imports = [MaterialModule, ApplicationModule];

  it('should a list of applications', async () => {
    // given
    const httpClientMock = jasmine.createSpyObj('HttpClient', {
      get: of(testApplications)
    });
    const providers = [
      {
        provide: HttpClient,
        useValue: httpClientMock
      }
    ];

    // when
    const component = await render(ApplicationOverviewComponent, {
      imports,
      providers
    });
    await component.fixture.whenStable();
    component.fixture.detectChanges();

    // then
    expect(httpClientMock.get).toHaveBeenCalledWith(
      environment.apiUrl + '/applications'
    );

    expect(component.getByRole('grid')).toBeDefined();

    expect(component.getByText(testApplications[0].name)).toBeDefined();
    expect(component.getByText(testApplications[1].name)).toBeDefined();
    expect(component.getByText(testApplications[2].name)).toBeDefined();

    expect(component.getByText(testApplications[0].description)).toBeDefined();
    expect(component.getByText(testApplications[1].description)).toBeDefined();
    expect(component.getByText(testApplications[2].description)).toBeDefined();

    expect(component.getByText(testApplications[0].contact)).toBeDefined();
    expect(component.getByText(testApplications[1].contact)).toBeDefined();
    expect(component.getByText(testApplications[2].contact)).toBeDefined();
  });

  it('should display a hint when no applications are available', async () => {
    // given
    const httpClientMock = jasmine.createSpyObj('HttpClient', { get: of([]) });
    const providers = [
      {
        provide: HttpClient,
        useValue: httpClientMock
      }
    ];

    // when
    const component = await render(ApplicationOverviewComponent, {
      imports,
      providers
    });
    await component.fixture.whenStable();
    component.fixture.detectChanges();

    // then
    expect(
      component.getByText('There are no applications available.')
    ).toBeDefined();
    expect(component.queryByRole('grid')).toBeNull();
    expect(component.queryByPlaceholderText('Filter')).toBeNull();
  });
});
