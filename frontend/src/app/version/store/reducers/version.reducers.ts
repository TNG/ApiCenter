import { VersionState } from '../state/version.state';
import { Version } from '../../../models/version';
import { Action, createReducer } from '@ngrx/store';

const initialState: VersionState = {
  versions: new Map<string, Version>()
};

const versionReducerCreator = createReducer(initialState);

export function versionReducer(
  state: VersionState | undefined,
  action: Action
) {
  return versionReducerCreator(state, action);
}
