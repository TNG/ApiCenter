import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, take } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { environment } from '../../environments/environment';
import { Application } from '../models/application';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  constructor(private httpClient: HttpClient, private snackBar: MatSnackBar) {}

  public async createApplication(application: Application) {
    return await this.httpClient
      .post(environment.apiUrl + '/applications', application)
      .pipe(
        take(1),
        catchError(
          this.getErrorHandler('Error creating application. Please try again.')
        )
      )
      .toPromise();
  }

  private getErrorHandler(errorMessage: string) {
    return (error: HttpErrorResponse) => {
      this.snackBar.open(errorMessage, 'close', { duration: 3000 });

      return throwError(error);
    };
  }
}
