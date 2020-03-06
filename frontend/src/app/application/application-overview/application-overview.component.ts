import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationCreateComponent } from '../application-create/application-create.component';
import { Application } from '../../models/application';
import { ApplicationService } from '../../services/application.service';

@Component({
  selector: 'app-application-overview',
  templateUrl: 'application-overview.component.html',
  styleUrls: ['application-overview.component.css']
})
export class ApplicationOverviewComponent implements OnInit {
  applications: Application[] = [];
  applicationsTableData: MatTableDataSource<Application>;

  displayedColumns = ['name', 'description', 'contact'];

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private applicationService: ApplicationService,
    private dialog: MatDialog
  ) {}

  async ngOnInit() {
    this.applications = await this.applicationService.getApplications();
    this.applicationsTableData = new MatTableDataSource<Application>(
      this.applications
    );
    this.applicationsTableData.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    this.applicationsTableData.filter = filterValue.trim().toLowerCase();
  }

  openCreateApplicationDialog() {
    this.dialog.open(ApplicationCreateComponent);
  }
}
