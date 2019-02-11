import {SpecificationService} from './specification.service';
import {HttpClient} from '@angular/common/http';
import {instance, mock, verify, when} from 'ts-mockito';
import {Specification} from './models/specification';
import {from} from 'rxjs/observable/from';
import 'rxjs/add/operator/catch';
import {SpecificationFile} from './models/specificationfile';
import {Version} from './models/version';

describe('SpecificationService', () => {

  let specificationService: SpecificationService;
  const mockedHttpClient = mock(HttpClient);
  const httpClient = instance(mockedHttpClient);
  const firstVersions = [new Version('1.0', 'Content')];
  const secondVersions = [new Version('v1', 'Content'),
    new Version('v2', 'Content')];
  const specifications = [new Specification('d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6', 'API 1', 'Description', firstVersions, null),
    new Specification('14dcb74e-f275-42fa-8f95-b26b3a4702c8', 'API 2', 'Description', secondVersions, 'http://address.com/test.json')];
  const swagger_content = '{\'swagger\': \'2.0\', \'info\': {\'version\': \'1.0.0\',\'title\': \'Swagger Petstore\'}}';
  const specificationFile = new SpecificationFile(swagger_content, null);

  beforeEach(() => {
    specificationService = new SpecificationService(httpClient);
  });

  it('should return specification', () => {
    when(mockedHttpClient.get('http://localhost:8080/api/1.0/specifications/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6'))
      .thenReturn(from([specifications[0]]));

    specificationService.getSpecification('d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6').subscribe((data: Specification) => {
      expect(data).toBe(specifications[0]);
    });
  });

  it('should create specification', () => {
    when(mockedHttpClient.post('http://localhost:8080/api/1.0/specifications', specificationFile))
      .thenReturn(from([specifications[0]]));

    specificationService.createSpecification(specificationFile).subscribe((data: Specification) => {
      expect(data).toBe(specifications[0]);
    });
  });

  it('should update specification', () => {
    when(mockedHttpClient.put('http://localhost:8080/api/1.0/specifications/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6', specificationFile))
      .thenReturn(from([specifications[0]]));

    specificationService.updateSpecification(specificationFile, 'd2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6').subscribe((data: Specification) => {
      expect(data).toBe(specifications[0]);
    });
  });

  it('should deleteSpecification specification', () => {
    when(mockedHttpClient.delete('http://localhost:8080/api/1.0/specifications/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6'))
      .thenReturn(from([]));

    specificationService.deleteSpecification('d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6').subscribe(() => {
      verify(mockedHttpClient.delete('http://localhost:8080/api/1.0/specifications/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6')).called();
    });
  });

  it('should synchronize specification', () => {
    when(mockedHttpClient.post('http://localhost:8080/api/1.0/specifications/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6/synchronize', ''))
      .thenReturn(from([]));

    specificationService.synchronizeSpecification('d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6').subscribe(() => {
      verify(mockedHttpClient
        .post('http://localhost:8080/api/1.0/specifications/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6/synchronize', ''))
        .called();
    });
  });
});
