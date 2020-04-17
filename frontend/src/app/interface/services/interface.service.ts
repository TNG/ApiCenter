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

  public createInterface(myInterface: Interface) {
    return this.httpClient.post<Interface[]>(
      environment.apiUrl + '/interfaces',
      myInterface
    );
  }

  public getInterface(id: string) {
    return this.httpClient.get<Interface>(
      environment.apiUrl + '/interfaces/' + id
    );
  }

  public updateInterface(myInterface: Interface) {
    return this.httpClient.put<Interface[]>(
      environment.apiUrl + '/interfaces/' + myInterface.id,
      myInterface
    );
  }

  public deleteInterface(id: string) {
    return this.httpClient.delete<Interface>(
      environment.apiUrl + '/interfaces/' + id
    );
  }
}
