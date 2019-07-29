import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../environments/environment';
import {ResultPage, Service} from './models/service';
import {SpecificationFile} from './models/specificationfile';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import {throwError} from 'rxjs';
import {Role} from './models/role';

@Injectable()
export class ServiceStore {

  private urlRoot = environment.apiUrl + '/service';

  constructor(private http: HttpClient) {
  }

  public getPage(pageNumber: number): Observable<ResultPage<Service>> {
    const params = new HttpParams().set('page', pageNumber.toString());
    return this.http.get<ResultPage<Service>>(this.urlRoot, {params: params})
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public getService(serviceId: string) {
    return this.http.get<Service>(this.urlRoot + '/' + serviceId)
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public createSpecifications(specificationFiles: SpecificationFile[]): Observable<Service> {
    return this.http.post<Service>(this.urlRoot, specificationFiles)
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

  public assignRoleForService(serviceId: string,
                              username: string,
                              role: Role
  ) {
    const params = new HttpParams().set('role', String(role));

    return this.http.put(this.urlRoot + '/' + serviceId + '/permissions/' + username, {}, {params})
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public removeRoleForService(serviceId: string,
                              username: string) {
    return this.http.delete(this.urlRoot + '/' + serviceId + '/permissions/' + username)
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public getRoleForService(serviceId: string,
                           username: string,
  ): Observable<Role> {
    return this.http.get<Role>(this.urlRoot + '/' + serviceId + '/permissions/' + username)
      .catch((error: any) => throwError(error || 'Server error'));
  }
}
