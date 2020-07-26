import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VersionOverviewComponent } from './version-overview/version-overview.component';
import { VersionTableComponent } from './version-table/version-table.component';
import { MaterialModule } from '../material/material.module';

@NgModule({
  declarations: [VersionOverviewComponent, VersionTableComponent],
  imports: [CommonModule, MaterialModule]
})
export class VersionModule {}
