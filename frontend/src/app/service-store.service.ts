import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../environments/environment';
import {Service} from './models/service';
import {SpecificationFile} from './models/specificationfile';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import {throwError} from 'rxjs';

@Injectable()
export class ServiceStore {

  private urlRoot = environment.apiUrl + '/service';

  constructor(private http: HttpClient) {
  }

  public getServices(): Observable<Service[]> {
    return this.http.get<Service[]>(this.urlRoot)
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public getService(serviceId: string) {
    return this.http.get<Service>(this.urlRoot + '/' + serviceId)
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public createSpecification(specificationFile: SpecificationFile): Observable<Service> {
    return this.http.post<Service>(this.urlRoot, specificationFile)
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public updateSpecification(specificationFile: SpecificationFile, serviceId: string): Observable<Service> {
    return this.http.put<Service>(this.urlRoot + '/' + serviceId, specificationFile)
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public deleteService(serviceId: string): Observable<Service> {
    return this.http.delete<Service>(this.urlRoot + '/' + serviceId)
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public synchronizeService(serviceId: string): Observable<Service> {
    return this.http.post<Service>(this.urlRoot + '/' + serviceId + '/synchronize', '')
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public searchForServices(searchString: string): Observable<Service[]> {
    return this.http.get<Service[]>(this.urlRoot + '/search/' + searchString)
      .catch((error: any) => throwError(error || 'Server error'));
  }

}
