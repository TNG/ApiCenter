import {Component, OnInit, Output} from '@angular/core';
import {Version} from '../models/version';
import {environment} from '../../environments/environment';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-version-view',
  template: '<app-graphiql *ngIf="specification && specification.language.toString() == \'GRAPHQL\'"></app-graphiql>' +
    '<app-swagger-ui [specification]="specification" ' +
    '*ngIf="specification && specification.language.toString() == \'OPENAPI\'"></app-swagger-ui>'
})

export class VersionViewComponent implements OnInit {
  @Output() specification: Version;

  constructor(protected route: ActivatedRoute, protected http: HttpClient) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.http.get<Version>(environment.apiUrl + '/specifications/' + params['specificationId'] + '/versions/' + params['version'])
        .subscribe(data => {
          this.specification = data;
        });
    });
  }
}
