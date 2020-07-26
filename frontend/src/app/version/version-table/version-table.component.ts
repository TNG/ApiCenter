import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  ViewChild
} from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Version } from '../../models/version';
import { Interface } from '../../models/interface';
import { Application } from '../../models/application';

@Component({
  selector: 'app-version-table',
  templateUrl: './version-table.component.html',
  styleUrls: ['./version-table.component.scss']
})
export class VersionTableComponent implements OnChanges {
  @Input() versions: (
    | Version
    | { interface: Interface }
    | { application: Application }
  )[];
  @Output() clickVersion = new EventEmitter<Version>();

  @ViewChild(MatSort, { static: false }) set sort(sort: MatSort) {
    this.dataSource.sort = sort;
  }

  dataSource: MatTableDataSource<
    Version | { interface: Interface } | { application: Application }
  >;
  displayedColumns: string[] = ['application', 'name', 'type', 'version'];

  constructor() {}

  ngOnChanges() {
    this.dataSource = new MatTableDataSource(this.versions);
    this.dataSource.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  onClickRow(row: Version) {
    this.clickVersion.emit(row);
  }
}
