import { createSelector } from '@ngrx/store';
import { AppState } from '../../../store/state/app.state';

const selectAppState = (state: AppState) => state;

export const selectVersionsWithInterfaceAndApplication = createSelector(
  selectAppState,
  (state: AppState) =>
    [...state.versionState.versions.values()]
      .map(version => ({
        ...version,
        interface: state.interfaceState.interfaces.get(version.interfaceId)
      }))
      .map(version => ({
        ...version,
        application: state.applicationState.applications.get(
          version.interface.applicationId
        )
      }))
);
