import { Component, Input, OnChanges, ViewChild } from '@angular/core';
import { Application } from '../../../models/application';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-application-table',
  templateUrl: './application-table.component.html',
  styleUrls: ['./application-table.component.scss']
})
export class ApplicationTableComponent implements OnChanges {
  @Input() applications: Application[];

  @ViewChild(MatSort, { static: false }) set sort(sort: MatSort) {
    this.dataSource.sort = sort;
  }

  dataSource: MatTableDataSource<Application>;
  displayedColumns: string[] = ['name', 'description', 'contact'];

  ngOnChanges() {
    this.dataSource = new MatTableDataSource(this.applications);
    this.dataSource.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
