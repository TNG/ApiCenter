import {SpecificationOverviewComponent} from './specification-overview.component';
import {instance, mock, verify, when} from 'ts-mockito';
import {SpecificationService} from '../specification.service';
import {Specification} from '../models/specification';
import {from} from 'rxjs/observable/from';

describe('SpecificationOverviewComponent', () => {

  let specificationOverviewComponent: SpecificationOverviewComponent;
  const mockedSpecificationService = mock(SpecificationService);
  const specificationService = instance(mockedSpecificationService);
  const specification = new Specification('b0fb472d-bee2-47b6-8ecf-ee5e1e76e990', 'Test', 'Description', '1.0', 'Content', null);

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
