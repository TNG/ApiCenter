import {SpecificationMetadata} from './specificationfile';

export enum ApiLanguage {
  OpenAPI = 'OPENAPI',
  GraphQL = 'GRAPHQL',
}

export enum ReleaseType {
  Release = 'RELEASE',
  Prerelease = 'PRERELEASE',
  Snapshot = 'SNAPSHOT',
}

export class Specification {
  content: string;
  metadata: SpecificationMetadata;

  constructor(content: string, metadata: SpecificationMetadata) {
    this.content = content;
    this.metadata = metadata;
  }
}
