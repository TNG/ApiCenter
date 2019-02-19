import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import * as SwaggerUI from 'swagger-ui';
import {Version} from '../models/version';

@Component({
  selector: 'app-specification',
  templateUrl: './specification-version.component.html',
  styleUrls: ['./specification-version.component.css']
})
export class SpecificationVersionComponent implements OnInit {
  error: String;
  specification: Version;

  constructor(private route: ActivatedRoute, private http: HttpClient) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.http.get<Version>(environment.apiUrl + '/specifications/' + params['specificationId'] + '/versions/' + params['version'])
        .subscribe(data => {
          this.specification = data;
          this.displaySwaggerUi();
        },
          err => {
            if (err.status === 404) {
              this.error = 'Version not found';
            }
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
