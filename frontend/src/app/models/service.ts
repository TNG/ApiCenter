import * as compareVersions from 'compare-versions';
import {Specification} from './specification';

export class Service {
  id: string;
  title: string;
  description: string;
  specifications: Specification[];
  remoteAddress: string;
  canEdit: boolean;

  constructor(id: string, title: string, description: string, specifications: Specification[], remoteAddress: string, canEdit: boolean) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.specifications = specifications;
    this.remoteAddress = remoteAddress;
    this.canEdit = canEdit;
  }

  public sortVersionsSemantically() {
    this.specifications.sort((spec1, spec2) =>
      -compareVersions(spec1.metadata.version, spec2.metadata.version));
  }
}

export interface ResultPage<T> {
  content: T[];
  last: boolean;
}
