import {SessionService} from './session.service';
import {instance, mock, when} from "ts-mockito";
import {HttpClient} from "@angular/common/http";
import {Token} from "./models/token";
import {from} from "rxjs";

describe('SessionService', () => {

  let sessionService: SessionService;
  const mockedHttpClient = mock(HttpClient);
  const httpClient = instance(mockedHttpClient);
  const token = new Token("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9");
  const loginReq = { username: "username", password: "password" };

  beforeEach(() => {
    sessionService = new SessionService(httpClient);
  });

  it('should return authentication token', () => {
    /*when(mockedHttpClient.post<Token>("http://localhost:8080/sessions", loginReq)).thenReturn(from([token]));

    sessionService.login("username", "password").subscribe((data: Token) => {
      expect(data).toBe(token);
    });*/
  });

});
