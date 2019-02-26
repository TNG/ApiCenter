import {SpecificationMetaData} from './specificationfile';

export enum ApiLanguage {
  OpenAPI = 'OPENAPI',
  GraphQL = 'GRAPHQL',
}

export class Version {
  content: string;
  metadata: SpecificationMetaData;

  constructor(content: string, metadata: SpecificationMetaData) {
    this.content = content;
    this.metadata = metadata;
  }
}
