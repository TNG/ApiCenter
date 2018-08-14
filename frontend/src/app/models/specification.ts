export class Specification {
  id: string;
  title: string;
  version: string;
  remoteAddress: string;

  constructor(id: string, title: string, version: string, remoteAddress: string) {
    this.id = id;
    this.title = title;
    this.version = version;
    this.remoteAddress = remoteAddress;
  }
}
