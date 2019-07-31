import {SpecificationOverviewComponent} from './specification-overview.component';
import {instance, mock, verify, when} from 'ts-mockito';
import {ServiceStore} from '../service-store.service';
import {Service} from '../models/service';
import {from} from 'rxjs/observable/from';
import {ApiLanguage, ReleaseType, Specification} from '../models/specification';
import {SpecificationStore} from '../specification-store.service';
import {Title} from '@angular/platform-browser';

describe('SpecificationOverviewComponent', () => {

  let specificationOverviewComponent: SpecificationOverviewComponent;
  const mockedServiceStore = mock(ServiceStore);
  const serviceStore = instance(mockedServiceStore);
  const mockedSpecificationStore = mock(SpecificationStore);
  const specificationStore = instance(mockedSpecificationStore);
  const title = instance(mock(Title));
  const specifications = [new Specification('Content', {
    title: 'Test',
    version: '1.0.0',
    description: 'Description',
    language: ApiLanguage.OpenAPI,
    releaseType: ReleaseType.Release,
    endpointUrl: null,
  })];
  const service = new Service('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990', 'Test', 'Description', specifications, null, false);

  beforeEach(() => {
    specificationOverviewComponent = new SpecificationOverviewComponent(serviceStore, specificationStore, title);
  });

  it('should synchronize file', () => {
    when(mockedServiceStore.synchronizeService('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990')).thenReturn(from([]));

    specificationOverviewComponent.synchronize(service).then(() => {
      verify(mockedServiceStore.synchronizeService('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990')).called();
    });
  });
});
