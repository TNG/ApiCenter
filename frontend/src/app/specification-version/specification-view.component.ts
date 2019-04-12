import {Component, OnInit, Output} from '@angular/core';
import {Specification} from '../models/specification';
import {environment} from '../../environments/environment';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-version-view',
  templateUrl: './version-view.component.html',
})

export class SpecificationViewComponent implements OnInit {
  @Output() specification: Specification;
  error: string;

  constructor(protected route: ActivatedRoute, protected http: HttpClient) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.http.get<Specification>(environment.apiUrl + '/specifications/' + params['specificationId'] + '/versions/' + params['version'])
        .subscribe(data => {
          this.specification = data;
        },
      err => {
          if (err.status === 404) {
            this.error = 'Specification not found';
          }
        });
    });
  }
}
