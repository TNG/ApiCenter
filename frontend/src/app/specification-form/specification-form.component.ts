import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SpecificationFile} from '../models/specificationfile';
import {SpecificationService} from '../specification.service';
import {Specification} from '../models/specification';

@Component({
  selector: 'app-specification-form',
  templateUrl: './specification-form.component.html',
  styleUrls: ['./specification-form.component.css'],
  providers: [SpecificationService]
})
export class SpecificationFormComponent implements OnInit {
  error: String;
  specificationFile: File;
  remoteUploadSelected = false;
  remoteFileUrl: string;
  gqlTitle: string;
  gqlVersion: string;
  gqlDescription: string;
  isGraphQLFile: boolean = false;

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
    const allFieldsPresent: boolean = !!this.gqlTitle && !!this.gqlVersion && !!this.gqlDescription;
    if (this.isGraphQLFile && !allFieldsPresent) {
      this.error = "All fields required for GraphQL upload";
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
      this.error = "No file selected";
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
    const file = new SpecificationFile(fileContent, fileUrl);

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

}
