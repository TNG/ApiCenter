import {ApiLanguage} from './specification';

export interface SpecificationMetadata {
  title: string;
  version: string;
  description: string;
  language: ApiLanguage;
  endpointUrl: string;
}

export class SpecificationFile {
  metadata?: SpecificationMetadata;
  fileContent: string;
  fileUrl: string;

  constructor(fileContent: string, fileUrl: string, metadata?: SpecificationMetadata) {
    this.fileContent = fileContent;
    this.fileUrl = fileUrl;
    if (metadata) {
      this.metadata = metadata;
    }
  }
}
