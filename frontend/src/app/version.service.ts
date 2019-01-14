import { Injectable } from '@angular/core';
import {environment} from '../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Specification} from './models/specification';
import {Observable, throwError} from 'rxjs';
import {Version} from './models/version';

@Injectable()
export class VersionService {

  private versionUrl = environment.apiUrl + '/specifications';

  constructor(private http: HttpClient) { }

  public getVersion(specificationId: string, version: string) {
    return this.http.get<Version>(this.versionUrl + '/' + specificationId + '/versions/' + version)
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public deleteVersion(specificationId: string, version: string): Observable<Version> {
    return this.http.delete<Version>(this.versionUrl + '/' + specificationId + '/versions/' + version)
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public downloadVersion(specificationId: string, version: string) {
    return this.http.get(environment.apiUrl + '/static/' + specificationId + '/versions/' + version, {responseType: 'text'})
  }
}
