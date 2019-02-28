import {SpecificationSearchDetailComponent} from './specification-search-detail.component';
import {SpecificationService} from '../specification.service';
import {instance, mock, verify, when} from 'ts-mockito';
import {ActivatedRoute} from '@angular/router';
import {Specification} from '../models/specification';
import {from} from 'rxjs/index';
import {ApiLanguage, Version} from '../models/version';

describe('SpecificationSearchDetailComponent', () => {
  let specificationSearchDetailComponent: SpecificationSearchDetailComponent;
  const mockedSpecificationService = mock(SpecificationService);
  const specificationService = instance(mockedSpecificationService);
  const mockedActivatedRoute = mock(ActivatedRoute);
  const activatedRoute = instance(mockedActivatedRoute);
  const versions = [new Version('Content', {
    title: 'Test',
    version: '1.0',
    description: 'Description',
    language: ApiLanguage.OpenAPI,
    server: null,
  })];
  const specifications = [new Specification('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990', 'Test', 'Description', versions, null)];

  beforeEach(() => {
    specificationSearchDetailComponent = new SpecificationSearchDetailComponent(specificationService, activatedRoute);
  });

  it('should show specifications', () => {
    when(mockedSpecificationService.searchSpecifications('Search String')).thenReturn(from([specifications]));

    const event = new KeyboardEvent('keyup', {
      code: '13'
    });

    specificationSearchDetailComponent.searchSpecifications(event).then(() => {
      verify(specificationService.searchSpecifications('Search String')).called();
      expect(specificationSearchDetailComponent.specifications).toBe(specifications);
    });
  });
});
