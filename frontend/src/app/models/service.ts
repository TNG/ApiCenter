import * as compareVersions from 'compare-versions';
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

  public sortVersionsSemantically() {
    this.specifications.sort((spec1, spec2) =>
      -compareVersions(spec1.metadata.version, spec2.metadata.version));
  }
}
