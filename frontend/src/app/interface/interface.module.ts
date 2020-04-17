import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InterfaceOverviewComponent } from './interface-overview/interface-overview.component';
import { InterfaceTableComponent } from './interface-table/interface-table.component';
import { MaterialModule } from '../material/material.module';
import { StoreModule } from '@ngrx/store';
import { interfaceReducer } from './store/reducers/interface.reducers';
import { EffectsModule } from '@ngrx/effects';
import { InterfaceEffects } from './store/effects/interface.effects';
import { InterfaceCreateComponent } from './interface-create/interface-create.component';
import { InterfaceFormComponent } from './interface-form/interface-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { InterfaceDetailsComponent } from './interface-details/interface-details.component';
import { InterfaceUpdateComponent } from './interface-update/interface-update.component';

@NgModule({
  declarations: [
    InterfaceOverviewComponent,
    InterfaceTableComponent,
    InterfaceCreateComponent,
    InterfaceFormComponent,
    InterfaceDetailsComponent,
    InterfaceUpdateComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    StoreModule.forFeature('interfaceState', interfaceReducer),
    EffectsModule.forFeature([InterfaceEffects]),
    ReactiveFormsModule
  ],
  entryComponents: [InterfaceCreateComponent, InterfaceUpdateComponent]
})
export class InterfaceModule {}
