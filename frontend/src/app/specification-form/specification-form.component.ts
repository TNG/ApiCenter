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
  manualDisabled = false;
  fileUrl: string;

  constructor(private router: Router, private specificationService: SpecificationService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.specificationService.getSpecification(params['id']).subscribe((specification: Specification) => {
        this.manualDisabled = specification.remoteAddress != null;
        this.fileUrl = specification.remoteAddress;
      });
    });
  }

  public getFile(event) {
    this.specificationFile = event.target.files[0];
  }

  public async submitSpecification(id?: string) {
    if (this.manualDisabled) {
      this.handleRemoteFile(id);
    } else {
      this.handleFile(id);
    }
  }

  public changeTypeRadio() {
    this.manualDisabled = !this.manualDisabled;
  }

  private handleRemoteFile(id: string) {
    if (!id) {
      this.createFile(null, this.fileUrl);
    } else {
      this.updateFile(null, this.fileUrl, id);
    }
  }

  private handleFile(id: string) {
    const reader = new FileReader();

    const me = this;

    reader.onload = function () {
      const text = reader.result;

      if (!id) {
        me.createFile(text, null);
      } else {
        me.updateFile(text, null, id);
      }
    };

    reader.readAsText(this.specificationFile);
  }

  private createFile(fileContent: string, fileUrl: string) {
    const file = new SpecificationFile(fileContent, fileUrl);

    this.specificationService.createSpecification(file)
      .subscribe(event => {
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.message);
  }

  private updateFile(fileContent: string, fileUrl: string, specificationId: string) {
    const file = new SpecificationFile(fileContent, fileUrl);

    this.specificationService.updateSpecification(file, specificationId)
      .subscribe(event => {
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.message);
  }

}
