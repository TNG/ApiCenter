import { createAction, props } from '@ngrx/store';
import { Application } from '../../../models/application';

export const loadApplications = createAction('Load applications');
export const loadApplicationsSuccess = createAction(
  'Load applications success',
  props<{ applications: Application[] }>()
);
export const loadApplicationsFailure = createAction(
  'Load applications failure'
);
