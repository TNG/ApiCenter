import {Component, OnInit} from '@angular/core';
import {ServiceStore} from '../service-store.service';
import {ApiLanguage, ReleaseType, Specification} from '../models/specification';
import {Page, PageOfServices, Service} from '../models/service';
import {SpecificationStore} from '../specification-store.service';
import {Title} from '@angular/platform-browser';
import {animate, state, style, transition, trigger} from '@angular/animations';

const pointingRight = {
  'transform': 'rotate(0)'
};

const pointingDown = {
  'transform': 'rotate(0.25turn)'
};

@Component({
  selector: 'app-specification-overview',
  templateUrl: './specification-overview.component.html',
  styleUrls: ['./specification-overview.component.css'],
  providers: [ServiceStore, SpecificationStore],
  animations: [
    trigger('rotateChevron', [
      state('pointRight', style(pointingRight)),
      state('pointDown', style(pointingDown)),
      transition('pointRight => pointDown', [
        animate('0.2s')
      ]),
      transition('pointDown => pointRight', [
        animate('0.2s')
      ])
    ])
  ]
})
export class SpecificationOverviewComponent implements OnInit {
  services: Service[] = [];
  error: string;
  expanded: string[] = [];
  displayShowMoreButton: boolean = false;
  pageNumber = -1;

  downloadFileFormatOptions: string[] = ['json', 'yaml'];
  selectedFormat: string = this.downloadFileFormatOptions[0];

  constructor(private serviceStore: ServiceStore,
              private specificationStore: SpecificationStore,
              private title: Title) {
    this.title.setTitle('Specification overview');
  }

  ngOnInit(): void {
    this.loadNextPage();
  }

  public async deleteService(service: Service) {
    if (confirm('Are you sure that you want to delete "' + service.title + '"?')) {
      this.serviceStore.deleteService(service.id)
        .subscribe(event => this.reloadAllPages());
    }
  }

  public async deleteSpecification(service: Service, specification: Specification) {
    if (confirm('Are you sure that you want to delete version "' + specification.metadata.version + '"?')) {
      this.specificationStore.deleteSpecification(service.id, specification.metadata.version).subscribe(event => {
          this.reloadAllPages();
          this.expanded = [];
        }
      );
    }
  }

  public async downloadService(service: Service, fileType: string) {
    this.downloadSpecification(fileType, service, this.getFirstRelease(service));
  }

  public downloadSpecification(fileType: string, service: Service, specification: Specification) {
    switch (specification.metadata.language) {
      case ApiLanguage.GraphQL:
        this.specificationStore.getSpecification(service.id, specification.metadata.version)
          .subscribe(event => {
            const fileName = this.createDownloadFileName(specification);
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
        this.downloadJson(specification);
        break;
      case 'yaml':
        this.downloadYaml(service, specification);
        break;
    }
  }

  public downloadJson(specification: Specification) {
    const fileName = this.createDownloadFileName(specification);
    const content = JSON.stringify(JSON.parse(specification.content), null, 2);
    this.doDownload(content, fileName + '.json', 'application/json');
  }

  public downloadYaml(service: Service, specification: Specification) {
    const fileName = this.createDownloadFileName(specification);
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

  private createDownloadFileName(specification: Specification) {
    // Make it filename safe by replacing any non-alphanumeric character with an underscore
    return (specification.metadata.title + '_v' + specification.metadata.version).replace(/[^a-z0-9\.]/gi, '_');
  }

  public async synchronize(service: Service) {
    return this.serviceStore.synchronizeService(service.id)
      .subscribe(event => this.reloadAllPages(),
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

  public loadNextPage() {
    const nextPage = this.pageNumber + 1;
    this.loadPage(nextPage).then(page => {
      this.services.push(...page.services);
      this.displayShowMoreButton = !page.isLast;
      this.pageNumber = nextPage;
    });
  }

  public reloadAllPages() {
    const pageRange = Array.from(Array(this.pageNumber + 1).keys());
    const promiseOfAllPages: Promise<PageOfServices[]> = Promise.all(pageRange.map(n => this.loadPage(n)));
    promiseOfAllPages.then(allPages => {
      this.services = allPages.map(page => page.services).flat();
      if (allPages.length > 0) {
        this.displayShowMoreButton = !allPages[allPages.length - 1].isLast;
      } else {
        this.displayShowMoreButton = false;
      }
    });
  }

  public getFirstRelease(service: Service): Specification {
    return (
      service.specifications
        .find(spec => spec.metadata.releaseType === ReleaseType.Release)
      || service.specifications[0]
    );
  }

  private async loadPage(pageNumber: number): Promise<PageOfServices> {
    return this.serviceStore.getPage(pageNumber).toPromise().then(
      (data: Page<Service>) => {

        const services: Service[] = data.content.map((element: Service) => {
          // An explicit constructor is required to use the Service class methods
          const service = new Service(element.id, element.title, element.description, element.specifications, element.remoteAddress);
          service.sortVersionsSemantically();
          return service;
        });

        return {services, isLast: data.last};
      },
      error => {
        if (error.status === 403) {
          this.error = 'You don\'t have permission to access content on this page';
        }
        return {services: [], isLast: true};
      }
    );
  }
}
