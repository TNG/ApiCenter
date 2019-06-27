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

export function inferReleaseType(version: string): ReleaseType {
  if (version.endsWith('-SNAPSHOT')) {
    return ReleaseType.Snapshot;
  } else if (/-BETA\d*/.test(version) || /-RC\d*/.test(version)) {
    return ReleaseType.Prerelease;
  } else {
    return ReleaseType.Release;
  }
}

export class Specification {
  content: string;
  metadata: SpecificationMetadata;

  constructor(content: string, metadata: SpecificationMetadata) {
    this.content = content;
    this.metadata = metadata;
  }
}
