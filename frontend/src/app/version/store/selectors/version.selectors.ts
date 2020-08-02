import { createSelector } from '@ngrx/store';
import { AppState } from '../../../store/state/app.state';
import { InterfaceState } from '../../../interface/store/state/interface.state';
import { VersionState } from '../state/version.state';

const selectAppState = (state: AppState) => state;
const selectVersionState = (state: AppState) => state.versionState;

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

export const selectVersion = createSelector(
  selectVersionState,
  (state: VersionState, { id }) => state.versions.get(id)
);
