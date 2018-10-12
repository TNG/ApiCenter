import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import * as SwaggerUI from 'swagger-ui';

@Component({
  selector: 'app-specification',
  templateUrl: './specification-version.component.html',
  styleUrls: ['./specification-version.component.css']
})
export class SpecificationVersionComponent implements OnInit {
  specification;

  constructor(private route: ActivatedRoute, private http: HttpClient) {
  }

  ngOnInit() {
    this.specification = this.route.params.subscribe(params => {
      this.http.get(environment.apiUrl + '/specifications/' + params['specificationId'] + '/versions/' + params['version'])
        .subscribe((data: any[]) => {
          this.specification = data;
          this.displaySwaggerUi();
        });
    });
  }

  private displaySwaggerUi() {
    SwaggerUI({
      spec: JSON.parse(this.specification.content),
      dom_id: '#display-swagger-ui'
    });
  }

}
