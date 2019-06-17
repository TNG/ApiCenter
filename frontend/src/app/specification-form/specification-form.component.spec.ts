import {SpecificationFormComponent} from './specification-form.component';
import {ActivatedRoute, Router} from '@angular/router';
import {anyOfClass, instance, mock, verify, when} from 'ts-mockito';
import {ServiceStore} from '../service-store.service';
import {SpecificationFile} from '../models/specificationfile';
import {from} from 'rxjs/observable/from';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

describe('SpecificationFormComponent', () => {

  let specificationFormComponent: SpecificationFormComponent;
  const mockedRouter = mock(Router);
  const router = instance(mockedRouter);
  const mockedServiceStore = mock(ServiceStore);
  const serviceStore = instance(mockedServiceStore);
  const mockedActivatedRoute = mock(ActivatedRoute);
  const modal = instance(mock(NgbModal));
  const activatedRoute = instance(mockedActivatedRoute);
  const swagger_content = '{\'swagger\': \'2.0\', \'info\': {\'version\': \'1.0.0\',\'title\': \'Swagger Petstore\'}}';
  const specificationFile = new SpecificationFile(swagger_content, null);

  beforeEach(() => {
    specificationFormComponent = new SpecificationFormComponent(router, serviceStore, activatedRoute, modal);
  });

  it('should create remote specification', () => {
    when(mockedServiceStore.createSpecification(anyOfClass(SpecificationFile))).thenReturn(from([]));

    specificationFormComponent.remoteFileUrl = 'https://testurl.com/file.json';
    specificationFormComponent.submitRemoteSpecification().then(() => {
      verify(mockedServiceStore.createSpecification(anyOfClass(SpecificationFile))).called();
    });
  });
});
