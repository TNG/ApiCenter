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

export interface Page<T> {
  // org.springframework.data.domain.Page

  content: T[];
  pageable: {
    sort: Sort;
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    unpaged: boolean;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: Sort;
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

interface Sort {
  unsorted: boolean;
  sorted: boolean;
  empty: boolean;
}
