import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Version } from '../../models/version';
import { Interface } from '../../models/interface';
import { Application } from '../../models/application';

@Component({
  selector: 'app-version-overview',
  templateUrl: './version-overview.component.html',
  styleUrls: ['./version-overview.component.scss']
})
export class VersionOverviewComponent implements OnInit {
  versionsWithInterfaceAndApplication$: Observable<
    Version | { interface: Interface } | { application: Application }
  >;

  constructor() {}

  ngOnInit() {}
}
