import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Interface } from '../../models/interface';

@Injectable({
  providedIn: 'root'
})
export class InterfaceService {
  constructor(private httpClient: HttpClient) {}

  public getInterfaces() {
    return this.httpClient.get<Interface[]>(environment.apiUrl + '/interfaces');
  }
}
