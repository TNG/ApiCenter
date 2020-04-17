import { createAction, props } from '@ngrx/store';
import { Interface } from '../../../models/interface';

export const loadInterfaces = createAction('Load interfaces');
export const loadInterfacesSuccess = createAction(
  'Load interfaces success',
  props<{ interfaces: Interface[] }>()
);
