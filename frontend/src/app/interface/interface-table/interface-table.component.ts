import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  ViewChild
} from '@angular/core';
import { Application } from '../../models/application';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Interface } from '../../models/interface';

@Component({
  selector: 'app-interface-table',
  templateUrl: './interface-table.component.html',
  styleUrls: ['./interface-table.component.scss']
})
export class InterfaceTableComponent implements OnChanges {
  @Input() interfaces: (Interface | { application: Application })[];
  @Output() clickInterface = new EventEmitter<Interface>();

  @ViewChild(MatSort, { static: false }) set sort(sort: MatSort) {
    this.dataSource.sort = sort;
  }

  dataSource: MatTableDataSource<Interface | { application: Application }>;
  displayedColumns: string[] = ['application', 'name', 'type'];

  ngOnChanges() {
    this.dataSource = new MatTableDataSource(this.interfaces);
    this.dataSource.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  onClickRow(row: Interface) {
    this.clickInterface.emit(row);
  }
}
