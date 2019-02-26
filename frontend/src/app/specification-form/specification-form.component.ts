import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SpecificationFile, SpecificationMetaData} from '../models/specificationfile';
import {SpecificationService} from '../specification.service';
import {Specification} from '../models/specification';
import {ApiLanguage} from '../models/version';

@Component({
  selector: 'app-specification-form',
  templateUrl: './specification-form.component.html',
  styleUrls: ['./specification-form.component.css'],
  providers: [SpecificationService]
})
export class SpecificationFormComponent implements OnInit {
  error: string;
  specificationFile: File;
  remoteUploadSelected = false;
  remoteFileUrl: string;
  additionalFields = {title: '', version: '', description: ''};
  servers: string[] = [];
  isGraphQLFile = false;
  objectKeys = Object.keys;

  constructor(private router: Router, private specificationService: SpecificationService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.specificationService.getSpecification(params['id']).subscribe((specification: Specification) => {
          this.remoteUploadSelected = specification.remoteAddress != null;
          this.remoteFileUrl = specification.remoteAddress;
        });
      }
    });
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
    const servers = this.servers;
    const metaData: SpecificationMetaData = this.isGraphQLFile ? {...this.additionalFields, language: ApiLanguage.GraphQL, servers} : null;
    const file = new SpecificationFile(fileContent, fileUrl, metaData);

    this.specificationService.createSpecification(file)
      .subscribe(event => {
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.message);
  }

  private updateSpecification(fileContent: string, fileUrl: string, specificationId: string) {
    const file = new SpecificationFile(fileContent, fileUrl);

    this.specificationService.updateSpecification(file, specificationId)
      .subscribe(event => {
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.message);
  }

  addServerInputField() {
    this.servers.push('');
  }

  removeServerInputField() {
    this.servers.pop();
  }

  trackByIndex(index: number) {
    // Without this, the wrong input element is focused after typing a character
    return index;
  }
}
