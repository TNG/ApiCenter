import { Injectable } from '@angular/core';
import * as jwt_decode from 'jwt-decode';

@Injectable()
export class AuthenticationService {

  getToken(): string {
    return localStorage.getItem('token');
  }

  getUsername(): string {
    return localStorage.getItem('username');
  }

  isTokenValid(token?: string): boolean {
    if (!token) {
      token = this.getToken();
    }

    if (!token) {
      return false;
    }

    try {
      const decoded = jwt_decode(token);
      return decoded.sub === this.getUsername();
    } catch (exception) {
      // jwt-decode throws InvalidTokenError if token incorrectly formatted
      return false;
    }
  }
}
