import { Injectable } from '@angular/core';
import {environment} from '../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {Version} from './models/version';

@Injectable()
export class VersionService {

  private versionUrl = environment.apiUrl + '/versions';

  constructor(private http: HttpClient) { }

  public getVersion(versionId: string) {
    return this.http.get<Version>(this.versionUrl + '/' + versionId)
      .catch((error: any) => throwError(error || 'Server error'));
  }

  public deleteVersion(versionId: string): Observable<Version> {
    return this.http.delete<Version>(this.versionUrl + '/' + versionId)
      .catch((error: any) => throwError(error || 'Server error'));
  }
}
