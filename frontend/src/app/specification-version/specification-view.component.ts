import {Component, OnInit, Output} from '@angular/core';
import {Specification} from '../models/specification';
import {environment} from '../../environments/environment';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-specification-view',
  templateUrl: './specification-view.component.html',
})

export class SpecificationViewComponent implements OnInit {
  @Output() specification: Specification;
  error: string;

  constructor(protected route: ActivatedRoute, protected http: HttpClient) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.http.get<Specification>(environment.apiUrl + '/service/' + params['serviceId'] + '/version/' + params['version'])
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
