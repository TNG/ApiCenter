import { ApplicationState } from '../state/application.state';
import { AppState } from '../../../store/state/app.state';
import { createSelector } from '@ngrx/store';

const selectApplicationState = (state: AppState) => state.applicationState;

export const selectApplications = createSelector(
  selectApplicationState,
  (state: ApplicationState) => [...state.applications.values()]
);

export const selectApplication = createSelector(
  selectApplicationState,
  (state: ApplicationState, { id }) => state.applications.get(id)
);
