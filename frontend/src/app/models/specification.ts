import {Version} from './version';

export class Specification {
  id: string;
  title: string;
  description: string;
  versions: Version[];
  remoteAddress: string;
  showEditButtons: boolean;

  constructor(id: string, title: string, description: string, versions: Version[], remoteAddress: string) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.versions = versions;
    this.remoteAddress = remoteAddress;
  }
}
