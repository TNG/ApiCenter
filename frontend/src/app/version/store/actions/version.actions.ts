import { createAction, props } from '@ngrx/store';
import { Version } from '../../../models/version';
import { VersionFile } from '../../../models/version-file';

export const loadVersions = createAction('Load versions');
export const loadVersionsSuccess = createAction(
  'Load versions success',
  props<{ versions: Version[] }>()
);
export const createVersion = createAction(
  'Create version',
  props<{ versionFile: VersionFile }>()
);
export const createVersionSuccess = createAction('Create version success');
