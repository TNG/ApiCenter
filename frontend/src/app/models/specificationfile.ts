import {ApiLanguage, ReleaseType} from './specification';

export interface SpecificationMetadata {
  title: string;
  version: string;
  description?: string;
  language: ApiLanguage;
  endpointUrl?: string;
  releaseType: ReleaseType;
}

export class SpecificationFile {
  metadata?: SpecificationMetadata;
  fileContent: string;
  fileUrl: string;
  id?: string;

  constructor(fileContent: string, fileUrl: string, id?: string, metadata?: SpecificationMetadata) {
    this.fileContent = fileContent;
    this.fileUrl = fileUrl;
    this.id = id;
    this.metadata = metadata;
  }
}
