import { ApplicationState } from '../state/application.state';
import { Action, createReducer, on } from '@ngrx/store';
import { loadApplicationsSuccess } from '../actions/application.actions';
import update from 'immutability-helper';

const initialState: ApplicationState = {
  applications: []
};

const applicationReducerCreator = createReducer(
  initialState,
  on(loadApplicationsSuccess, (state, { applications }) =>
    update(state, { applications: { $set: applications } })
  )
);

export function applicationReducer(
  state: ApplicationState | undefined,
  action: Action
) {
  return applicationReducerCreator(state, action);
}
