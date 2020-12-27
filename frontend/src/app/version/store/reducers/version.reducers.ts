import { VersionState } from '../state/version.state';
import { Version } from '../../../models/version';
import { Action, createReducer, on } from '@ngrx/store';
import update from 'immutability-helper';
import {
  loadVersionsSuccess,
  loadVersionSuccess
} from '../actions/version.actions';

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
  }),
  on(loadVersionSuccess, (state, { version }) => {
    console.log('yo!', version);
    return update(state, {
      versions: { $add: [[version.id, version]] }
    });
  })
);

export function versionReducer(
  state: VersionState | undefined,
  action: Action
) {
  return versionReducerCreator(state, action);
}
