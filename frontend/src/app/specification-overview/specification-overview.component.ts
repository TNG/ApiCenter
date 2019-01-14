import {Component, OnInit} from '@angular/core';
import {SpecificationService} from '../specification.service';
import {Specification} from '../models/specification';
import {VersionService} from '../version.service';

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


  private downloadVersion = {
    json: this.downloadJSONVersion,
    yml: this.downloadYMLVersion,
  };

  public async downloadSpecification(specification, fileType: string) {
    // Download the latest version of this specification
    const firstVersion = specification.versions[0];
    if (firstVersion !== undefined) {
      this.downloadVersion[fileType](specification, firstVersion)
    }
  }

  public async downloadJSONVersion(specification, version) {
    const fileName = this.createDownloadFileName(specification, version);

    // As the JSON specification versions are already available in the browser memory,
    // we do not need to involve the backend server to download one

    // JSON will be in human-readable format / 'pretty-printed'
    const content = JSON.stringify(JSON.parse(version.content), null, 2);
    this.doDownload(content, fileName + '.json', 'application/json');
  }

  public async downloadYMLVersion(specification, version) {
    const fileName = this.createDownloadFileName(specification, version);

    // I don't think I need to call getSpecifications in the subscription, because the list of specifications will not change with a download operation (?)
    this.versionService.downloadVersion(specification.id, version.version)
      .subscribe(content => {
        this.doDownload(content, fileName + '.yml', 'application/yaml');
      })
  }

  private createDownloadFileName(specification, version) {
    // Make it filename safe by replacing any non-alphanumeric character with an underscore
    return (specification.title + '_v' + version.version).replace(/[^a-z0-9\.]/gi, '_');
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
    this.specificationService.getSpecifications().subscribe((data: Specification[]) => this.specifications = data);
  }

  private doDownload(contents: string, fileName: string, type: string) {
    // For compatibility reasons the file download does not use the data URI scheme
    const anchor = window.document.createElement('a');
    anchor.href = window.URL.createObjectURL(new Blob([contents], {type}));
    // version.content cannot be passed directly to the blob, as it has double quotes escaped

    anchor.download = fileName;

    // Append anchor to body.
    document.body.appendChild(anchor);
    anchor.click();

    // Remove anchor from body
    document.body.removeChild(anchor);
  }
}
