import { Component, Input, OnInit } from '@angular/core';

declare const SwaggerUIBundle: any;

@Component({
  selector: 'app-openapi-ui',
  templateUrl: './openapi-ui.component.html',
  styleUrls: ['./openapi-ui.component.scss']
})
export class OpenapiUiComponent implements OnInit {
  @Input() spec: any;

  constructor() {}

  ngOnInit() {
    SwaggerUIBundle({
      dom_id: '#swagger-ui',
      layout: 'BaseLayout',
      spec: this.spec
    });
  }
}
