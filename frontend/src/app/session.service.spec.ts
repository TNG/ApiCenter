import {SessionService} from './session.service';
import {instance, mock, when} from 'ts-mockito';
import {HttpClient} from '@angular/common/http';
import {SessionToken} from './models/sessiontoken';

describe('SessionService', () => {

  let sessionService: SessionService;
  const mockedHttpClient = mock(HttpClient);
  const httpClient = instance(mockedHttpClient);
  const token = new SessionToken('Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9');
  const loginReq = { username: 'username', password: 'password' };

  beforeEach(() => {
    sessionService = new SessionService(httpClient);
  });

  /*it('should return authentication token', () => {
    when(mockedHttpClient.post('http://localhost:8080/sessions', loginReq)).thenReturn(from([token]));

    sessionService.login('username', 'password').subscribe((data: SessionToken) => {
      expect(data).toBe(token);
    });
  });*/

});
