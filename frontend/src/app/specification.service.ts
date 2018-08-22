import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../environments/environment';
import {Specification} from './models/specification';
import {SpecificationFile} from './models/specificationfile';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

@Injectable()
export class SpecificationService {

  private specificationsUrl = environment.apiUrl + '/specifications';

  constructor(private http: HttpClient) {
  }

  public getSpecifications(): Observable<Specification[]> {
    return this.http.get<Specification[]>(this.specificationsUrl)
      .catch((error: any) => Observable.throw(error || 'Server error'));
  }

  public getSpecification(specificationId: string) {
    return this.http.get<Specification>(this.specificationsUrl + '/' + specificationId)
      .catch((error: any) => Observable.throw(error || 'Server error'));
  }

  public createSpecification(specificationFile: SpecificationFile): Observable<Specification> {
    return this.http.post<Specification>(this.specificationsUrl, specificationFile)
      .catch((error: any) => Observable.throw(error || 'Server error'));
  }

  public updateSpecification(specificationFile: SpecificationFile, specificationId: string): Observable<Specification> {
    return this.http.put<Specification>(this.specificationsUrl + '/' + specificationId, specificationFile)
      .catch((error: any) => Observable.throw(error || 'Server error'));
  }

  public deleteSpecification(specificationId: string): Observable<Specification> {
    return this.http.delete<Specification>(this.specificationsUrl + '/' + specificationId)
      .catch((error: any) => Observable.throw(error || 'Server error'));
  }

  public synchronizeSpecification(specificationId: string): Observable<Specification> {
    return this.http.post<Specification>(this.specificationsUrl + '/' + specificationId + '/synchronize', '')
      .catch((error: any) => Observable.throw(error || 'Server error'));
  }

}
