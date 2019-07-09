import {ApiLanguage, ReleaseType} from './specification';

export interface SpecificationMetadata {
  title: string;
  version: string;
  description?: string;
  language: ApiLanguage;
  endpointUrl?: string;
  releaseType: ReleaseType;
}

export interface SpecificationFileMetadata {
  title: string;
  version: string;
  description: string;
  language: ApiLanguage;
  endpointUrl: string;
}

export class SpecificationFile {
  metadata?: SpecificationFileMetadata;
  fileContent: string;
  fileUrl: string;
  id?: string;

  constructor(fileContent: string, fileUrl: string, id?: string, metadata?: SpecificationFileMetadata) {
    this.fileContent = fileContent;
    this.fileUrl = fileUrl;
    this.id = id;
    this.metadata = metadata;
  }
}
