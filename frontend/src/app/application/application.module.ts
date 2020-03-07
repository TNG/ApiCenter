import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationCreateComponent } from './application-create/application-create.component';
import { ApplicationOverviewComponent } from './application-overview/application-overview.component';
import { ApplicationFormComponent } from './application-form/application-form.component';
import { MaterialModule } from '../material/material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { StoreModule } from '@ngrx/store';
import { applicationReducer } from './store/reducers/application.reducers';
import { EffectsModule } from '@ngrx/effects';
import { ApplicationEffects } from './store/effects/application.effects';

@NgModule({
  declarations: [
    ApplicationCreateComponent,
    ApplicationOverviewComponent,
    ApplicationFormComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    StoreModule.forFeature('applicationState', applicationReducer),
    EffectsModule.forFeature([ApplicationEffects])
  ],
  entryComponents: [ApplicationCreateComponent],
  exports: [ApplicationOverviewComponent]
})
export class ApplicationModule {}
