import { createAction, props } from '@ngrx/store';
import { Interface } from '../../../models/interface';

export const loadInterfaces = createAction('Load interfaces');
export const loadInterfacesSuccess = createAction(
  'Load interfaces success',
  props<{ interfaces: Interface[] }>()
);

export const loadInterface = createAction(
  'Load interface',
  props<{ id: string }>()
);
export const loadInterfaceSuccess = createAction(
  'Load interface success',
  props<{ interface: Interface }>()
);

export const createInterface = createAction(
  'Create interface',
  props<{ interface: Interface }>()
);
export const createInterfaceSuccess = createAction('Create interface success');

export const updateInterface = createAction(
  'Update interface',
  props<{ interface: Interface }>()
);
export const updateInterfaceSuccess = createAction('Update interface success');
