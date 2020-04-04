import { ApplicationState } from '../state/application.state';
import { Action, createReducer, on } from '@ngrx/store';
import { loadApplicationsSuccess } from '../actions/application.actions';
import update from 'immutability-helper';
import { Application } from '../../../models/application';

const initialState: ApplicationState = {
  applications: new Map<string, Application>()
};

const applicationReducerCreator = createReducer(
  initialState,
  on(loadApplicationsSuccess, (state, { applications }) => {
    const applicationMap: Map<string, Application> = new Map<
      string,
      Application
    >(applications.map(application => [application.id, application]));
    return update(state, { applications: { $set: applicationMap } });
  })
);

export function applicationReducer(
  state: ApplicationState | undefined,
  action: Action
) {
  return applicationReducerCreator(state, action);
}
