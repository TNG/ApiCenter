export enum ApiLanguage {
  OpenAPI = 'OPENAPI',
  GraphQL = 'GRAPHQL',
}

export class Version {
  version: string;
  content: string;
  language: ApiLanguage;

  constructor(version: string, content: string, language: ApiLanguage) {
    this.version = version;
    this.content = content;
    this.language = language;
  }
}
