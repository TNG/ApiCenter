import { AppState } from '../../store/state/app.state';
import { createSelector } from '@ngrx/store';
import { InterfaceState } from '../store/state/interface.state';

const selectInterfaceState = (state: AppState) => state.interfaceState;
const selectAppState = (state: AppState) => state;

export const selectInterfaces = createSelector(
  selectInterfaceState,
  (state: InterfaceState) => [...state.interfaces.values()]
);

export const selectInterfacesWithApplications = createSelector(
  selectAppState,
  (state: AppState) =>
    [...state.interfaceState.interfaces.values()].map(myInterface => ({
      ...myInterface,
      application: state.applicationState.applications.get(
        myInterface.applicationId
      )
    }))
);

export const selectInterface = createSelector(
  selectInterfaceState,
  (state: InterfaceState, { id }) => state.interfaces.get(id)
);
