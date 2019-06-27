import {ServiceStore} from './service-store.service';
import {HttpClient} from '@angular/common/http';
import {instance, mock, verify, when} from 'ts-mockito';
import {Service} from './models/service';
import {from} from 'rxjs/observable/from';
import 'rxjs/add/operator/catch';
import {SpecificationFile} from './models/specificationfile';
import {ApiLanguage, ReleaseType, Specification} from './models/specification';

describe('ServiceStore', () => {

  let serviceStore: ServiceStore;
  const mockedHttpClient = mock(HttpClient);
  const httpClient = instance(mockedHttpClient);

  const metadataStub = {
    version: '1.0.0',
    description: 'Description',
    language: ApiLanguage.OpenAPI,
    releaseType: ReleaseType.Release,
    endpointUrl: null,
  };

  const specifications1 = [new Specification('Content', {...metadataStub, title: 'API 1'})];
  const specifications2 = [
    new Specification('Content', {...metadataStub, title: 'API 2', version: 'v1'}),
    new Specification('Content', {...metadataStub, title: 'API 2', version: 'v2'}),
  ];

  const services = [
    new Service('d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6', 'API 1', 'Description', specifications1, null),
    new Service('14dcb74e-f275-42fa-8f95-b26b3a4702c8', 'API 2', 'Description', specifications2, 'http://address.com/test.json')
  ];

  const swagger_content = '{\'swagger\': \'2.0\', \'info\': {\'version\': \'1.0.0\',\'title\': \'Swagger Petstore\'}}';
  const specificationFile = new SpecificationFile(swagger_content, null);

  beforeEach(() => {
    serviceStore = new ServiceStore(httpClient);
  });

  it('should return service', () => {
    when(mockedHttpClient.get('http://localhost:8080/api/v1/service/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6'))
      .thenReturn(from([services[0]]));

    serviceStore.getService('d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6').subscribe((data: Service) => {
      expect(data).toBe(services[0]);
    });
  });

  it('should update specification', () => {
    when(mockedHttpClient.put('http://localhost:8080/api/v1/service/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6', specificationFile))
      .thenReturn(from([services[0]]));

    serviceStore.updateSpecification(specificationFile, 'd2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6').subscribe((data: Service) => {
      expect(data).toBe(services[0]);
    });
  });

  it('should delete service', () => {
    when(mockedHttpClient.delete('http://localhost:8080/api/v1/service/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6'))
      .thenReturn(from([]));

    serviceStore.deleteService('d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6').subscribe(() => {
      verify(mockedHttpClient.delete('http://localhost:8080/api/v1/service/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6')).called();
    });
  });

  it('should synchronize service', () => {
    when(mockedHttpClient.post('http://localhost:8080/api/v1/service/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6/synchronize', ''))
      .thenReturn(from([]));

    serviceStore.synchronizeService('d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6').subscribe(() => {
      verify(mockedHttpClient
        .post('http://localhost:8080/api/v1/service/d2317ad4-b6b4-4bc5-a3cc-7eed72eeedb6/synchronize', ''))
        .called();
    });
  });
});
