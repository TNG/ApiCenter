export enum APILanguage {
  OpenAPI,
  GraphQL
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
