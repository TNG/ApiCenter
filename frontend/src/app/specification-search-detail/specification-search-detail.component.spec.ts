import {SpecificationSearchDetailComponent} from './specification-search-detail.component';
import {ServiceStore} from '../service-store.service';
import {instance, mock, verify, when} from 'ts-mockito';
import {ActivatedRoute} from '@angular/router';
import {Service} from '../models/service';
import {from} from 'rxjs/index';
import {ApiLanguage, ReleaseType, Specification} from '../models/specification';

describe('SpecificationSearchDetailComponent', () => {
  let specificationSearchDetailComponent: SpecificationSearchDetailComponent;
  const mockedSpecificationService = mock(ServiceStore);
  const specificationService = instance(mockedSpecificationService);
  const mockedActivatedRoute = mock(ActivatedRoute);
  const activatedRoute = instance(mockedActivatedRoute);
  const specifications = [new Specification('Content', {
    title: 'Test',
    version: '1.0',
    description: 'Description',
    language: ApiLanguage.OpenAPI,
    releaseType: ReleaseType.Release,
    endpointUrl: null,
  })];
  const services = [new Service('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990', 'Test', 'Description', specifications, null)];

  beforeEach(() => {
    specificationSearchDetailComponent = new SpecificationSearchDetailComponent(specificationService, activatedRoute);
  });

  it('should show services', () => {
    when(mockedSpecificationService.searchForServices('Search String')).thenReturn(from([services]));

    const event = new KeyboardEvent('keyup', {
      code: '13'
    });

    specificationSearchDetailComponent.searchServices(event).then(() => {
      verify(specificationService.searchForServices('Search String')).called();
      expect(specificationSearchDetailComponent.services).toBe(services);
    });
  });
});
