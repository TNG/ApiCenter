import {Specification} from './specification';

export class Service {
  id: string;
  title: string;
  description: string;
  specifications: Specification[];
  remoteAddress: string;

  constructor(id: string, title: string, description: string, specifications: Specification[], remoteAddress: string) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.specifications = specifications;
    this.remoteAddress = remoteAddress;
  }
}
