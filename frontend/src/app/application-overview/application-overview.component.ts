import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Application } from '../models/application';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-application-overview',
  templateUrl: 'application-overview.component.html',
  styleUrls: ['application-overview.component.css']
})
export class ApplicationOverviewComponent implements OnInit {

  applications: Application[] = [
    {name: 'CRM', description: 'Customer management', contact: 'Max Mustermann'},
    {name: 'Webportal', description: 'Online shop and self-service', contact: 'Martina Musterfrau'},
    {name: 'WFE', description: 'Workflow engine processing orders of customers', contact: 'Bernd das Brot'},
  ];

  displayedColumns: string[] = ['name', 'description', 'contact'];
  dataSource = new MatTableDataSource(this.applications);

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  ngOnInit() {
    this.dataSource.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}
