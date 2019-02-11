import { Injectable } from '@angular/core';
import {environment} from '../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Specification} from './models/specification';
import {Observable, throwError} from 'rxjs';
import {Version} from './models/version';

@Injectable()
export class VersionService {

  private versionUrl = environment.apiUrl + '/specifications';

  constructor(private http: HttpClient) { }

  public getYAMLVersion(specificationId: string, version: string) {
    const headers = new HttpHeaders({'Accept': 'application/yml'});
    return this.http.get<Version>(this.versionUrl + '/' + specificationId + '/versions/' + version, {headers})
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public deleteVersion(specificationId: string, version: string): Observable<Version> {
    return this.http.delete<Version>(this.versionUrl + '/' + specificationId + '/versions/' + version)
      .catch((error: any) => throwError(error || 'Server error'));
  }
}
