import {SpecificationFormComponent} from './specification-form.component';
import {ActivatedRoute, Router} from '@angular/router';
import {anyOfClass, instance, mock, verify, when} from 'ts-mockito';
import {SpecificationService} from '../specification.service';
import {SpecificationFile} from '../models/specificationfile';
import {from} from 'rxjs/observable/from';

describe('SpecificationFormComponent', () => {

  let specificationFormComponent: SpecificationFormComponent;
  const mockedRouter = mock(Router);
  const router = instance(mockedRouter);
  const mockedSpecificationService = mock(SpecificationService);
  const specificationService = instance(mockedSpecificationService);
  const mockedActivatedRoute = mock(ActivatedRoute);
  const activatedRoute = instance(mockedActivatedRoute);
  const swagger_content = '{\'swagger\': \'2.0\', \'info\': {\'version\': \'1.0.0\',\'title\': \'Swagger Petstore\'}}';
  const specificationFile = new SpecificationFile(swagger_content, null);

  beforeEach(() => {
    specificationFormComponent = new SpecificationFormComponent(router, specificationService, activatedRoute);
  });

  it('should change enabled form parts', () => {
    expect(specificationFormComponent.manualDisabled).toBe(false);
    specificationFormComponent.changeTypeRadio();
    expect(specificationFormComponent.manualDisabled).toBe(true);
  });

  it('should create remote specification', () => {
    when(mockedSpecificationService.createSpecification(anyOfClass(SpecificationFile))).thenReturn(from([]));

    specificationFormComponent.manualDisabled = true;
    specificationFormComponent.fileUrl = 'https://testurl.com/file.json';
    specificationFormComponent.submitSpecification(null).then(() => {
      verify(mockedSpecificationService.createSpecification(anyOfClass(SpecificationFile))).called();
    });
  });

  it('should update remote specification', () => {
    when(mockedSpecificationService.updateSpecification(anyOfClass(SpecificationFile), 'baa15785-58cd-4e54-9f29-fcbad8962208'))
      .thenReturn(from([]));

    specificationFormComponent.manualDisabled = true;
    specificationFormComponent.fileUrl = 'https://testurl.com/file.json';
    specificationFormComponent.submitSpecification('baa15785-58cd-4e54-9f29-fcbad8962208').then(() => {
      verify(mockedSpecificationService.updateSpecification(anyOfClass(SpecificationFile), 'baa15785-58cd-4e54-9f29-fcbad8962208'))
        .called();
    });
  });
});
