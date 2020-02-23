import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Application } from '../models/application';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationFormComponent } from '../application-form/application-form.component';

@Component({
  selector: 'app-application-overview',
  templateUrl: 'application-overview.component.html',
  styleUrls: ['application-overview.component.css']
})
export class ApplicationOverviewComponent implements OnInit {
  applications: Application[] = [
    {
      name: 'CRM',
      description: 'Customer management',
      contact: 'Max Mustermann'
    },
    {
      name: 'Webportal',
      description: 'Online shop and self-service',
      contact: 'Martina Musterfrau'
    },
    {
      name: 'WFE',
      description: 'Workflow engine processing orders of customers',
      contact: 'Bernd das Brot'
    }
  ];

  displayedColumns = ['name', 'description', 'contact'];
  applicationsTableData = new MatTableDataSource<Application>(
    this.applications
  );

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(private dialog: MatDialog) {}

  ngOnInit() {
    this.applicationsTableData.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    this.applicationsTableData.filter = filterValue.trim().toLowerCase();
  }

  openCreateApplicationDialog() {
    this.dialog.open(ApplicationFormComponent, {
      height: '400px',
      width: '600px'
    });
  }
}
