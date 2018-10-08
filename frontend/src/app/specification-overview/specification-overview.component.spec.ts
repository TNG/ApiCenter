import {SpecificationOverviewComponent} from './specification-overview.component';
import {instance, mock, verify, when} from 'ts-mockito';
import {SpecificationService} from '../specification.service';
import {Specification} from '../models/specification';
import {from} from 'rxjs/observable/from';
import {Version} from '../models/version';

describe('SpecificationOverviewComponent', () => {

  let specificationOverviewComponent: SpecificationOverviewComponent;
  const mockedSpecificationService = mock(SpecificationService);
  const specificationService = instance(mockedSpecificationService);
  const versions = [new Version('0bb3274f-4d8b-405d-8148-4288bab65289', '1.0', 'Content')];
  const specification = new Specification('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990', 'Test', 'Description', versions, null);

  beforeEach(() => {
    specificationOverviewComponent = new SpecificationOverviewComponent(specificationService);
  });

  it('should snychronize file', () => {
    when(mockedSpecificationService.synchronizeSpecification('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990')).thenReturn(from([]));

    specificationOverviewComponent.synchronize(specification).then(() => {
      verify(mockedSpecificationService.synchronizeSpecification('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990')).called();
    });
  });
});
