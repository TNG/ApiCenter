import {EventEmitter, Injectable, Output} from '@angular/core';
import {SessionToken} from './models/sessiontoken';

@Injectable()
export class LoginEvent {
  @Output() eventEmitter: EventEmitter<any> = new EventEmitter<any>();

  public changeValue(sessionToken: SessionToken) {
    this.eventEmitter.emit(sessionToken);
  }

  public getValue() {
    return this.eventEmitter;
  }
}
