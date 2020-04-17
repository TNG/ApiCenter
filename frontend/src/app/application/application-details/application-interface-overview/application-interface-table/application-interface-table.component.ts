import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  ViewChild
} from '@angular/core';
import { Interface } from '../../../../models/interface';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-application-interface-table',
  templateUrl: './application-interface-table.component.html',
  styleUrls: ['./application-interface-table.component.scss']
})
export class ApplicationInterfaceTableComponent implements OnChanges {
  @Input() interfaces: Interface[];
  @Output() clickInterface = new EventEmitter<Interface>();

  @ViewChild(MatSort, { static: false }) set sort(sort: MatSort) {
    this.dataSource.sort = sort;
  }

  dataSource: MatTableDataSource<Interface>;
  displayedColumns: string[] = ['name', 'type'];

  ngOnChanges() {
    this.dataSource = new MatTableDataSource(this.interfaces);
    this.dataSource.sort = this.sort;
  }

  onClickRow(row: Interface) {
    this.clickInterface.emit(row);
  }
}
