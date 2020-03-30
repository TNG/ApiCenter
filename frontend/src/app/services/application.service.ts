import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { environment } from '../../environments/environment';
import { Application } from '../models/application';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  constructor(private httpClient: HttpClient, private snackBar: MatSnackBar) {}

  public createApplication(application: Application) {
    return this.httpClient.post<Application[]>(
      environment.apiUrl + '/applications',
      application
    );
  }

  public updateApplication(application: Application) {
    return this.httpClient.put<Application[]>(
      environment.apiUrl + '/applications/' + application.id,
      application
    );
  }

  public deleteApplication(application: Application) {
    return this.httpClient.delete<Application>(
      environment.apiUrl + '/applications/' + application.id
    );
  }

  public getApplications() {
    return this.httpClient.get<Application[]>(
      environment.apiUrl + '/applications'
    );
  }
}
