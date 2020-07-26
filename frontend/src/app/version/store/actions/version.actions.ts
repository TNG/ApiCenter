import { createAction, props } from '@ngrx/store';
import { Version } from '../../../models/version';

export const loadVersions = createAction('Load versions');
export const loadVersionsSuccess = createAction(
  'Load versions success',
  props<{ versions: Version[] }>()
);
