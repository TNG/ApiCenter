import {Component, OnInit} from '@angular/core';
import {SpecificationService} from '../specification.service';
import {Specification} from '../models/specification';
import {Version} from '../models/version';
import {VersionService} from '../version.service';

/* tslint:disable:member-ordering */
@Component({
  selector: 'app-specification-overview',
  templateUrl: './specification-overview.component.html',
  styleUrls: ['./specification-overview.component.css'],
  providers: [SpecificationService, VersionService]
})
export class SpecificationOverviewComponent implements OnInit {
  specifications: Specification[];
  error: String;
  expanded: String[] = [];

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
    if (confirm('Are you sure that you want to delete version "' + version.version + '"?')) {
      this.versionService.deleteVersion(specification.id, version.version).subscribe(event => {
          this.getSpecifications();
          this.expanded = [];
        }
      );
    }
  }

  public async downloadSpecification(specification: Specification, fileType: string) {
    const latestVersion = specification.versions[0];
    if (latestVersion !== undefined) {
      this.downloadVersion[fileType](specification, latestVersion);
    }
  }

  private createDownloadFileName(specification: Specification, version: Version) {
    // Make it filename safe by replacing any non-alphanumeric character with an underscore
    return (specification.title + '_v' + version.version).replace(/[^a-z0-9\.]/gi, '_');
  }

  public downloadJSONVersion = async (specification: Specification, version: Version) => {
    const fileName = this.createDownloadFileName(specification, version);
    const content = JSON.stringify(JSON.parse(version.content), null, 2);
    this.doDownload(content, fileName + '.json', 'application/json');
  }

  public downloadYAMLVersion = async (specification: Specification, version: Version) => {
    const fileName = this.createDownloadFileName(specification, version);
    this.versionService.getYAMLVersion(specification.id, version.version)
      .subscribe(event => {
        this.doDownload(event.content, fileName + '.yml', 'application/yaml');
      });
  }

  downloadVersion = {
    // Avoids code duplication in this.downloadSpecification
    json: this.downloadJSONVersion,
    yml: this.downloadYAMLVersion,
  };

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
    this.specificationService.getSpecifications().subscribe((data: Specification[]) => this.specifications = data);
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
}
