import { AppState } from '../../store/state/app.state';
import { createSelector } from '@ngrx/store';
import { InterfaceState } from '../store/state/interface.state';
import { Interface } from '../../models/interface';

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

export const selectInterfacesById = createSelector(
  selectInterfaceState,
  (state: InterfaceState, { interfaceIds }) => {
    const selectedInterfaces: Interface[] = [];

    state.interfaces.forEach((value: Interface, key: string) => {
      if (interfaceIds.includes(key)) {
        selectedInterfaces.push(value);
      }
    });

    return selectedInterfaces;
  }
);
