export class Specification {
  id: string;
  title: string;
  description: string;
  version: string;
  content: string;
  remoteAddress: string;

  constructor(id: string, title: string, description: string, version: string, content: string, remoteAddress: string) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.version = version;
    this.content = content;
    this.remoteAddress = remoteAddress;
  }
}
