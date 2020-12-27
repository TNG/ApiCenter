import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Version } from '../../models/version';
import { VersionFile } from '../../models/version-file';

@Injectable({
  providedIn: 'root'
})
export class VersionService {
  constructor(private httpClient: HttpClient) {}

  public getVersions() {
    return this.httpClient.get<Version[]>(environment.apiUrl + '/versions');
  }

  public getVersion(id: string) {
    return this.httpClient.get<Version>(environment.apiUrl + '/versions/' + id);
  }

  public createVersion(versionFile: VersionFile) {
    return this.httpClient.post<Version>(
      environment.apiUrl + '/versions',
      versionFile
    );
  }

  public deleteVersion(id: string) {
    return this.httpClient.delete<Version>(
      environment.apiUrl + '/versions/' + id
    );
  }
}
