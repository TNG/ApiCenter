import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Interface } from '../../models/interface';
import { environment } from '../../../environments/environment';
import { Version } from '../../models/version';

@Injectable({
  providedIn: 'root'
})
export class VersionService {
  constructor(private httpClient: HttpClient) {}

  public getVersions() {
    return this.httpClient.get<Version[]>(environment.apiUrl + '/versions');
  }
}
