import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";
import {Observable, throwError} from "rxjs";
import {Token} from "./models/token";

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private sessionsUrl = environment.apiUrl + '/sessions';

  constructor(private httpClient: HttpClient) { }

  public login(username: string, password: string): Observable<Token> {
    const loginRequest = { username: username, password: password };

    return this.httpClient.post<Token>(this.sessionsUrl, loginRequest)
      .catch((error: any) => throwError(error || 'Server error'));
  }
}
