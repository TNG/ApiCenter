import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../environments/environment';
import {Observable, throwError} from 'rxjs';
import {SessionToken} from './models/sessiontoken';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private sessionsUrl = environment.apiUrl + '/sessions';

  constructor(private httpClient: HttpClient) { }

  public login(username: string, password: string): Observable<SessionToken> {
    const loginRequest = { username: username, password: password };

    return this.httpClient.post<SessionToken>(this.sessionsUrl, loginRequest)
      .catch((error: any) => throwError(error || 'Server error'));
  }
}
