import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VersionOverviewComponent } from './version-overview/version-overview.component';
import { VersionTableComponent } from './version-table/version-table.component';
import { MaterialModule } from '../material/material.module';
import { StoreModule } from '@ngrx/store';
import { versionReducer } from './store/reducers/version.reducers';
import { EffectsModule } from '@ngrx/effects';
import { VersionEffects } from './store/effects/version.effects';
import { VersionDetailsComponent } from './version-details/version-details.component';
import { OpenapiUiComponent } from './version-details/openapi-ui/openapi-ui.component';
import { VersionCreateComponent } from './version-create/version-create.component';

@NgModule({
  declarations: [
    VersionOverviewComponent,
    VersionTableComponent,
    VersionDetailsComponent,
    OpenapiUiComponent,
    VersionCreateComponent
  ],
  imports: [
    CommonModule,
    EffectsModule.forFeature([VersionEffects]),
    MaterialModule,
    StoreModule.forFeature('versionState', versionReducer)
  ],
  entryComponents: [VersionCreateComponent]
})
export class VersionModule {}
