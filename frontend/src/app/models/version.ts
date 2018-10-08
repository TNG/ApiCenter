export class Version {
  id: string;
  version: string;
  content: string;

  constructor(id: string, version: string, content: string) {
    this.id = id;
    this.version = version;
    this.content = content;
  }
}
