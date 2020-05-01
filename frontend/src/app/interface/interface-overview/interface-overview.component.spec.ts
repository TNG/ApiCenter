import { InterfaceOverviewComponent } from './interface-overview.component';
import { render } from '@testing-library/angular';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../material/material.module';
import { StoreModule } from '@ngrx/store';
import { interfaceReducer } from '../store/reducers/interface.reducers';
import { EffectsModule } from '@ngrx/effects';
import { InterfaceEffects } from '../store/effects/interface.effects';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { InterfaceTableComponent } from '../interface-table/interface-table.component';

describe('InterfaceOverviewComponent', () => {
  const imports = [
    CommonModule,
    MaterialModule,
    StoreModule.forRoot({ interfaceState: interfaceReducer }),
    EffectsModule.forRoot([InterfaceEffects]),
    ReactiveFormsModule,
    RouterTestingModule,
    HttpClientTestingModule
  ];

  const declarations = [InterfaceTableComponent];

  it('should show list of interfaces', async () => {
    const component = await render(InterfaceOverviewComponent, {
      imports,
      declarations
    });
  });
});
