import {Component, OnInit} from '@angular/core';
import {SpecificationService} from '../specification.service';
import {Specification} from '../models/specification';
import {ApiLanguage, Version} from '../models/version';
import {VersionService} from '../version.service';

@Component({
  selector: 'app-specification-overview',
  templateUrl: './specification-overview.component.html',
  styleUrls: ['./specification-overview.component.css'],
  providers: [SpecificationService, VersionService]
})
export class SpecificationOverviewComponent implements OnInit {
  specifications: Specification[];
  error: string;
  expanded: string[] = [];

  downloadFileFormatOptions: string[] = ['json', 'yaml'];
  selectedFormat: string = this.downloadFileFormatOptions[0];

  constructor(private specificationService: SpecificationService, private versionService: VersionService) {
  }

  ngOnInit(): void {
    this.getSpecifications();
  }

  public async deleteSpecification(specification) {
    if (confirm('Are you sure that you want to delete "' + specification.title + '"?')) {
      this.specificationService.deleteSpecification(specification.id)
        .subscribe(event => this.getSpecifications());
    }
  }

  public async deleteVersion(specification, version) {
    if (confirm('Are you sure that you want to delete version "' + version.metadata.version + '"?')) {
      this.versionService.deleteVersion(specification.id, version.metadata.version).subscribe(event => {
          this.getSpecifications();
          this.expanded = [];
        }
      );
    }
  }

  public async downloadSpecification(specification: Specification, fileType: string) {
    this.downloadVersion(fileType, specification, specification.versions[0]);
  }

  public downloadVersion(fileType: string, spec: Specification, version: Version) {
    switch (version.metadata.language) {
      case ApiLanguage.GraphQL:
        this.versionService.getVersion(spec.id, version.metadata.version)
          .subscribe(event => {
            const fileName = this.createDownloadFileName(spec, version);
            this.doDownload(event.content, fileName + '.graphql', 'application/json');
          });
        break;
      case ApiLanguage.OpenAPI:
        this.downloadOpenApiVersion(fileType, spec, version);
        break;
    }
  }

  public downloadOpenApiVersion(fileType: string, spec: Specification, version: Version) {
    switch (fileType) {
      case 'json':
        this.downloadJsonVersion(spec, version);
        break;
      case 'yaml':
        this.downloadYamlVersion(spec, version);
        break;
    }
  }

  public downloadJsonVersion(specification: Specification, version: Version) {
    const fileName = this.createDownloadFileName(specification, version);
    const content = JSON.stringify(JSON.parse(version.content), null, 2);
    this.doDownload(content, fileName + '.json', 'application/json');
  }

  public downloadYamlVersion(specification: Specification, version: Version) {
    const fileName = this.createDownloadFileName(specification, version);
    this.versionService.getYamlVersion(specification.id, version.metadata.version)
      .subscribe(event => {
        this.doDownload(event.content, fileName + '.yml', 'application/yaml');
      });
  }

  private doDownload(contents: string, fileName: string, type: string) {
    // For compatibility reasons the file download does not use the data URI scheme
    const anchor = window.document.createElement('a');
    anchor.href = window.URL.createObjectURL(new Blob([contents], {type}));
    // version.content cannot be passed directly to the blob, as it has double quotes escaped

    anchor.download = fileName;
    document.body.appendChild(anchor);
    anchor.click();
    document.body.removeChild(anchor);
  }

  private createDownloadFileName(specification: Specification, version: Version) {
    // Make it filename safe by replacing any non-alphanumeric character with an underscore
    return (specification.title + '_v' + version.metadata.version).replace(/[^a-z0-9\.]/gi, '_');
  }

  public async synchronize(specification) {
    return this.specificationService.synchronizeSpecification(specification.id)
      .subscribe(event => this.getSpecifications());
  }

  public async switchExpanded(specification) {
    if (this.expanded.includes(specification.id)) {
      this.expanded.splice(this.expanded.indexOf(specification.id), 1);
    } else {
      this.expanded.push(specification.id);
    }
  }

  private async getSpecifications() {
    this.specificationService.getSpecifications().subscribe(
     (data: Specification[]) => this.specifications = data,
     error1 => {
       if (error1.status === 403) {
         this.error = 'You don\'t have permission to access content on this page';
       }
     }
   );
  }
}
