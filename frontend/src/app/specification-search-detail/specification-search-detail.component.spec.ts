import {SpecificationSearchDetailComponent} from './specification-search-detail.component';
import {ServiceStore} from '../service-store.service';
import {instance, mock, verify, when} from 'ts-mockito';
import {ActivatedRoute} from '@angular/router';
import {Service} from '../models/service';
import {from} from 'rxjs/index';
import {ApiLanguage, Specification} from '../models/specification';

describe('SpecificationSearchDetailComponent', () => {
  let specificationSearchDetailComponent: SpecificationSearchDetailComponent;
  const mockedSpecificationService = mock(ServiceStore);
  const specificationService = instance(mockedSpecificationService);
  const mockedActivatedRoute = mock(ActivatedRoute);
  const activatedRoute = instance(mockedActivatedRoute);
  const versions = [new Specification('Content', {
    title: 'Test',
    version: '1.0',
    description: 'Description',
    language: ApiLanguage.OpenAPI,
    endpointUrl: null,
  })];
  const specifications = [new Service('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990', 'Test', 'Description', versions, null)];

  beforeEach(() => {
    specificationSearchDetailComponent = new SpecificationSearchDetailComponent(specificationService, activatedRoute);
  });

  it('should show specifications', () => {
    when(mockedSpecificationService.searchForServices('Search String')).thenReturn(from([specifications]));

    const event = new KeyboardEvent('keyup', {
      code: '13'
    });

    specificationSearchDetailComponent.searchSpecifications(event).then(() => {
      verify(specificationService.searchForServices('Search String')).called();
      expect(specificationSearchDetailComponent.services).toBe(specifications);
    });
  });
});
