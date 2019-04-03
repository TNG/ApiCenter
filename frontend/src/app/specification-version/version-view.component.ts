import {Component, OnInit, Output} from '@angular/core';
import {Version} from '../models/version';
import {environment} from '../../environments/environment';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-version-view',
  templateUrl: './version-view.component.html',
})

export class VersionViewComponent implements OnInit {
  @Output() specification: Version;
  error: string;

  constructor(protected route: ActivatedRoute, protected http: HttpClient) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.http.get<Version>(environment.apiUrl + '/specifications/' + params['specificationId'] + '/versions/' + params['version'])
        .subscribe(data => {
          this.specification = data;
        },
      error => this.error = error.error.userMessage
      );
    });
  }
}
