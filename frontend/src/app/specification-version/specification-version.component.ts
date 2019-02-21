import {AfterViewChecked, AfterViewInit, Component, Input, OnChanges, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import * as SwaggerUI from 'swagger-ui';
import {Version} from '../models/version';
import {VersionViewComponent} from './version-view.component';

@Component({
  selector: 'app-specification',
  templateUrl: './specification-version.component.html',
  styleUrls: ['./specification-version.component.css']
})
export class SpecificationVersionComponent extends VersionViewComponent implements OnChanges {
  @Input() specification: Version;

  constructor(route: ActivatedRoute, http: HttpClient) {
    super(route, http);
  }

  ngOnChanges() {
    if (this.specification) {
      this.displaySwaggerUi();
    }
  }

  private displaySwaggerUi() {
    SwaggerUI({
      spec: JSON.parse(this.specification.content),
      dom_id: '#display-swagger-ui'
    });
  }

}
