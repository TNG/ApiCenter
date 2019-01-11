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

  public async downloadSpecification(specification) {
    // Download the latest version of this specification
    const firstVersion = specification.versions[0];
    if (firstVersion !== undefined) {
      this.downloadVersion(specification, firstVersion)
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

  public async downloadVersion(specification, version) {
    // As the JSON specification versions are already available in the browser memory,
    // we do not need to involve the backend server to download one

    // // Should the JSON be in human-readable format/'pretty-printed'?
    // const parsedVersion = JSON.parse(version.content)
    // const versionStringified = JSON.stringify(parsedVersion, null, 2)

    // // For compatibility reasons the file download does not use the data URI scheme
    // const anchor = window.document.createElement('a');
    // anchor.href = window.URL.createObjectURL(new Blob([versionStringified], {type: 'application/json'}));
    // // version.content cannot be passed directly to the blob, as it has double quotes escaped

    // // Construct a filename
    // const fileName = (parsedVersion.info.title + '_v' + parsedVersion.info.version).replace(/[^a-z0-9\.]/gi, '_') + '.json'
    // // Make it filename safe by replacing any non-alphanumeric character with an underscore
    // anchor.download = fileName;

    // // Append anchor to body.
    // document.body.appendChild(anchor);
    // anchor.click();

    // // Remove anchor from body
    // document.body.removeChild(anchor);

    // Using the server
    // I don't think I need to subscribe to getSpecifications, because the list of specifications will not change with a download operation (?)
    this.versionService.downloadVersion(specification.id, version.version)
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
}
