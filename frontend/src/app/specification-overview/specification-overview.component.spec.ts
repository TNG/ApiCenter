import {SpecificationOverviewComponent} from './specification-overview.component';
import {instance, mock, verify, when} from 'ts-mockito';
import {ServiceStore} from '../service-store.service';
import {Service} from '../models/service';
import {from} from 'rxjs/observable/from';
import {ApiLanguage, Specification} from '../models/specification';
import {SpecificationStore} from '../specification-store.service';

describe('SpecificationOverviewComponent', () => {

  let specificationOverviewComponent: SpecificationOverviewComponent;
  const mockedSpecificationService = mock(ServiceStore);
  const specificationService = instance(mockedSpecificationService);
  const mockedVersionService = mock(SpecificationStore);
  const versionService = instance(mockedVersionService);
  const versions = [new Specification('Content', {
    title: 'Test',
    version: '1.0.0',
    description: 'Description',
    language: ApiLanguage.OpenAPI,
    endpointUrl: null,
  })];
  const specification = new Service('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990', 'Test', 'Description', versions, null);

  beforeEach(() => {
    specificationOverviewComponent = new SpecificationOverviewComponent(specificationService, versionService);
  });

  it('should snychronize file', () => {
    when(mockedSpecificationService.synchronizeService('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990')).thenReturn(from([]));

    specificationOverviewComponent.synchronize(specification).then(() => {
      verify(mockedSpecificationService.synchronizeService('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990')).called();
    });
  });
});
