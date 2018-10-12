export class SessionToken {
  token: string;
  username: string;

  constructor(token: string, username: string) {
    this.token = token;
    this.username = username;
  }
}
