export enum APILanguage {
  OpenAPI = 'OPENAPI',
  GraphQL = 'GRAPHQL',
}

export class Version {
  version: string;
  content: string;
  language: APILanguage;

  constructor(version: string, content: string, language: APILanguage) {
    this.version = version;
    this.content = content;
    this.language = language;
  }
}
