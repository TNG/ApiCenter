import { ApplicationState } from '../state/application.state';
import { AppState } from '../../../store/state/app.state';
import { createSelector } from '@ngrx/store';

const selectApplicationState = (state: AppState) => state.applicationState;

export const selectApplications = createSelector(
  selectApplicationState,
  (state: ApplicationState) => state.applications
);

export const selectApplication = createSelector(
  selectApplicationState,
  (state: ApplicationState, { id }) =>
    state.applications.find(application => application.id === id)
);
