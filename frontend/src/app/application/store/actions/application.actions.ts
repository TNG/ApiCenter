import { createAction, props } from '@ngrx/store';
import { Application } from '../../../models/application';

export const loadApplications = createAction('Load applications');
export const loadApplicationsSuccess = createAction(
  'Load applications success',
  props<{ applications: Application[] }>()
);

export const createApplication = createAction(
  'Create application',
  props<{ application: Application }>()
);
export const createApplicationSuccess = createAction(
  'Create application success'
);

export const deleteApplication = createAction(
  'Delete application',
  props<{ application: Application }>()
);

export const deleteApplicationSuccess = createAction(
  'Delete application success'
);
