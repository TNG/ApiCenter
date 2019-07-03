import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SpecificationFile, SpecificationMetadata} from '../models/specificationfile';
import {ServiceStore} from '../service-store.service';
import {Service} from '../models/service';
import {ApiLanguage} from '../models/specification';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {animate, style, transition, trigger} from '@angular/animations';

const hiddenStyle = {
  'max-height': '0px',
  'visibility': 'hidden',
  'opacity': '0'
};

const visibleStyle = {
  'max-height': '300px',
  'visibility': 'visible',
  'opacity': '1'
};

@Component({
  selector: 'app-specification-form',
  templateUrl: './specification-form.component.html',
  styleUrls: ['./specification-form.component.css'],
  providers: [ServiceStore],
  animations: [
    trigger('displayFields', [
      transition(':enter', [
        style(hiddenStyle),
        animate('200ms ease-in', style(visibleStyle))
      ]),
      transition(':leave', [
        animate('200ms ease-in', style(hiddenStyle))
      ])
    ])
  ]
})
export class SpecificationFormComponent implements OnInit {
  error: string;
  specificationFile: File;
  remoteFileUrl: string;
  additionalFields = {title: '', version: '', description: '', endpointUrl: '', id: ''};
  isGraphQLFile = false;
  objectKeys = Object.keys;

  constructor(private router: Router,
              private serviceStore: ServiceStore,
              private route: ActivatedRoute,
              private modalService: NgbModal) {
  }

  ngOnInit() {
  }

  private open(content) {
    this.isGraphQLFile = false;
    // Otherwise, GraphQL metadata fields persist when the modal is closed and reopened,
    // even though the file is no longer selected.

    this.modalService.open(content, {}).result.then(
      () => {
        // modal is closed
        this.error = undefined;
      },
      () => {
        // modal is dismissed
        this.error = undefined;
      }
      );
  }

  public onLocalFileChange(event) {
    this.specificationFile = event.target.files[0];
    this.isGraphQLFile = /.*\.graphql/.test(this.specificationFile.name);
  }

  public async submitLocalSpecification() {
    const allFieldsPresent: boolean = !!this.additionalFields.title && !!this.additionalFields.version;
    if (this.isGraphQLFile && !allFieldsPresent) {
      this.error = 'Title and version are required';
      return;
    }
    this.handleLocalFile();
  }

  public async submitRemoteSpecification() {
    this.handleRemoteFile();
  }

  private handleLocalFile() {
    if (!this.specificationFile) {
      this.error = 'No file selected';
      return;
    }

    const reader = new FileReader();
    const me = this;

    reader.onload = function () {
      const text = reader.result.toString();
      me.createSpecification(text, null);
    };

    reader.readAsText(this.specificationFile);
  }

  private handleRemoteFile() {
    this.createSpecification(null, this.remoteFileUrl);
  }

  private createSpecification(fileContent: string, fileUrl: string) {
    const metadata: SpecificationMetadata = this.isGraphQLFile ?
      {...this.additionalFields, language: ApiLanguage.GraphQL} : null;
    const file = new SpecificationFile(fileContent, fileUrl, metadata);

    this.serviceStore.createSpecification(file)
      .subscribe(event => {
          this.modalService.dismissAll();
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.userMessage);
  }

}
