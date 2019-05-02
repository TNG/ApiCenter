import {Component, OnInit} from '@angular/core';
import {ServiceStore} from '../service-store.service';
import {Service} from '../models/service';
import {ApiLanguage, Specification} from '../models/specification';
import {SpecificationStore} from '../specification-store.service';

@Component({
  selector: 'app-specification-overview',
  templateUrl: './specification-overview.component.html',
  styleUrls: ['./specification-overview.component.css'],
  providers: [ServiceStore, SpecificationStore]
})
export class SpecificationOverviewComponent implements OnInit {
  services: Service[];
  error: string;
  expanded: string[] = [];

  downloadFileFormatOptions: string[] = ['json', 'yaml'];
  selectedFormat: string = this.downloadFileFormatOptions[0];

  constructor(private serviceStore: ServiceStore, private specificationStore: SpecificationStore) {
  }

  ngOnInit(): void {
    this.getServices();
  }

  public async deleteService(service: Service) {
    if (confirm('Are you sure that you want to delete "' + service.title + '"?')) {
      this.serviceStore.deleteService(service.id)
        .subscribe(event => this.getServices());
    }
  }

  public async deleteSpecification(service: Service, specification: Specification) {
    if (confirm('Are you sure that you want to delete version "' + specification.metadata.version + '"?')) {
      this.specificationStore.deleteSpecification(service.id, specification.metadata.version).subscribe(event => {
          this.getServices();
          this.expanded = [];
        }
      );
    }
  }

  public async downloadService(service: Service, fileType: string) {
    this.downloadSpecification(fileType, service, service.specifications[0]);
  }

  public downloadSpecification(fileType: string, service: Service, specification: Specification) {
    switch (specification.metadata.language) {
      case ApiLanguage.GraphQL:
        this.specificationStore.getSpecification(service.id, specification.metadata.version)
          .subscribe(event => {
            const fileName = this.createDownloadFileName(service, specification);
            this.doDownload(event.content, fileName + '.graphql', 'application/json');
          });
        break;
      case ApiLanguage.OpenAPI:
        this.downloadOpenApi(fileType, service, specification);
        break;
    }
  }

  public downloadOpenApi(fileType: string, service: Service, specification: Specification) {
    switch (fileType) {
      case 'json':
        this.downloadJson(service, specification);
        break;
      case 'yaml':
        this.downloadYaml(service, specification);
        break;
    }
  }

  public downloadJson(service: Service, specification: Specification) {
    const fileName = this.createDownloadFileName(service, specification);
    const content = JSON.stringify(JSON.parse(specification.content), null, 2);
    this.doDownload(content, fileName + '.json', 'application/json');
  }

  public downloadYaml(service: Service, specification: Specification) {
    const fileName = this.createDownloadFileName(service, specification);
    this.specificationStore.getYamlSpecification(service.id, specification.metadata.version)
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

  private createDownloadFileName(service: Service, specification: Specification) {
    // Make it filename safe by replacing any non-alphanumeric character with an underscore
    return (service.title + '_v' + specification.metadata.version).replace(/[^a-z0-9\.]/gi, '_');
  }

  public async synchronize(service: Service) {
    return this.serviceStore.synchronizeService(service.id)
      .subscribe(event => this.getServices(),
        error => {
          this.error = error.error.userMessage;
        });
  }

  public async switchExpanded(service: Service) {
    if (this.expanded.includes(service.id)) {
      this.expanded.splice(this.expanded.indexOf(service.id), 1);
    } else {
      this.expanded.push(service.id);
    }
  }

  private async getServices() {
    this.serviceStore.getServices().subscribe(
     (data: Service[]) => this.services = data,
     error1 => {
       if (error1.status === 403) {
         this.error = 'You don\'t have permission to access content on this page';
       }
     }
   );
  }
}
