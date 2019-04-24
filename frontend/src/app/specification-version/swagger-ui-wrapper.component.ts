import {Component, Input, OnChanges} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import * as SwaggerUI from 'swagger-ui';
import {Specification} from '../models/specification';
import {SpecificationViewComponent} from './specification-view.component';
import {SpecificationStore} from '../specification-store.service';

@Component({
  selector: 'app-swagger-ui',
  template: '<div id="display-swagger-ui"></div>',
  styleUrls: ['./swagger-ui-wrapper.component.css']
})
export class SwaggerUiWrapperComponent extends SpecificationViewComponent implements OnChanges {
  @Input() specification: Specification;

  constructor(route: ActivatedRoute, specificationStore: SpecificationStore) {
    super(route, specificationStore);
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
