import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationCreateComponent } from './application-create/application-create.component';
import { ApplicationOverviewComponent } from './application-overview/application-overview.component';
import { ApplicationFormComponent } from './application-form/application-form.component';
import { MaterialModule } from '../material/material.module';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    ApplicationCreateComponent,
    ApplicationOverviewComponent,
    ApplicationFormComponent
  ],
  imports: [CommonModule, MaterialModule, ReactiveFormsModule],
  entryComponents: [ApplicationCreateComponent],
  exports: [ApplicationOverviewComponent]
})
export class ApplicationModule {}
