import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SpecificationFile, SpecificationMetadata} from '../models/specificationfile';
import {ServiceStore} from '../service-store.service';
import {Service} from '../models/service';
import {ApiLanguage} from '../models/specification';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-specification-form',
  templateUrl: './specification-form.component.html',
  styleUrls: ['./specification-form.component.css'],
  providers: [ServiceStore]
})
export class SpecificationFormComponent implements OnInit {
  error: string;
  specificationFile: File;
  remoteUploadSelected = false;
  remoteFileUrl: string;
  additionalFields = {title: '', version: '', description: ''};
  endpointUrl = '';
  isGraphQLFile = false;
  objectKeys = Object.keys;

  constructor(private router: Router,
              private serviceStore: ServiceStore,
              private route: ActivatedRoute,
              private modalService: NgbModal) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.serviceStore.getService(params['id']).subscribe((service: Service) => {
          this.remoteUploadSelected = service.remoteAddress != null;
          this.remoteFileUrl = service.remoteAddress;
        });
      }
    });
  }

  private open(content) {
    this.modalService.open(content, {}).result.then(
      () => {},
      () => {}
      );
  }

  public onLocalFileChange(event) {
    this.specificationFile = event.target.files[0];
    this.isGraphQLFile = /.*\.graphql/.test(this.specificationFile.name);
  }

  public async submitSpecification(id?: string) {
    const allFieldsPresent: boolean = !!this.additionalFields.title && !!this.additionalFields.version;
    if (this.isGraphQLFile && !allFieldsPresent) {
      this.error = 'Title and version are required';
      return;
    }

    if (this.remoteUploadSelected) {
      this.handleRemoteFile(id);
    } else {
      this.handleLocalFile(id);
    }
  }

  public changeTypeRadio() {
    this.remoteUploadSelected = !this.remoteUploadSelected;
  }

  private handleRemoteFile(id: string) {
    if (!id) {
      this.createSpecification(null, this.remoteFileUrl);
    } else {
      this.updateSpecification(null, this.remoteFileUrl, id);
    }
  }

  private handleLocalFile(id: string) {
    if (!this.specificationFile) {
      this.error = 'No file selected';
      return;
    }

    const reader = new FileReader();
    const me = this;

    reader.onload = function () {
      const text = reader.result;

      if (!id) {
        me.createSpecification(text, null);
      } else {
        me.updateSpecification(text, null, id);
      }
    };

    reader.readAsText(this.specificationFile);
  }

  private createSpecification(fileContent: string, fileUrl: string) {
    const endpointUrl = this.endpointUrl;
    const metadata: SpecificationMetadata = this.isGraphQLFile ?
      {...this.additionalFields, language: ApiLanguage.GraphQL, endpointUrl} : null;
    const file = new SpecificationFile(fileContent, fileUrl, metadata);

    this.serviceStore.createSpecification(file)
      .subscribe(event => {
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.userMessage);
  }

  private updateSpecification(fileContent: string, fileUrl: string, specificationId: string) {
    const file = new SpecificationFile(fileContent, fileUrl);

    this.serviceStore.updateSpecification(file, specificationId)
      .subscribe(event => {
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.userMessage);
  }
}
