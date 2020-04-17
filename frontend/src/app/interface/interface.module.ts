import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InterfaceOverviewComponent } from './interface-overview/interface-overview.component';
import { InterfaceTableComponent } from './interface-table/interface-table.component';
import { MaterialModule } from '../material/material.module';
import { StoreModule } from '@ngrx/store';
import { interfaceReducer } from './store/reducers/interface.reducers';
import { EffectsModule } from '@ngrx/effects';
import { InterfaceEffects } from './store/effects/interface.effects';

@NgModule({
  declarations: [InterfaceOverviewComponent, InterfaceTableComponent],
  imports: [
    CommonModule,
    MaterialModule,
    StoreModule.forFeature('interfaceState', interfaceReducer),
    EffectsModule.forFeature([InterfaceEffects])
  ]
})
export class InterfaceModule {}
