import { VersionState } from '../state/version.state';
import { Version } from '../../../models/version';
import { Action, createReducer, on } from '@ngrx/store';
import update from 'immutability-helper';
import { loadVersionsSuccess } from '../actions/version.actions';

const initialState: VersionState = {
  versions: new Map<string, Version>()
};

const versionReducerCreator = createReducer(
  initialState,
  on(loadVersionsSuccess, (state, { versions }) => {
    const versionMap: Map<string, Version> = new Map<string, Version>(
      versions.map(version => [version.id, version])
    );
    return update(state, { versions: { $set: versionMap } });
  })
);

export function versionReducer(
  state: VersionState | undefined,
  action: Action
) {
  return versionReducerCreator(state, action);
}
