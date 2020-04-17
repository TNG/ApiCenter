import { Action, createReducer, on } from '@ngrx/store';
import { InterfaceState } from '../state/interface.state';
import { Interface } from '../../../models/interface';
import update from 'immutability-helper';
import { loadInterfacesSuccess } from '../actions/interface.actions';

const initialState: InterfaceState = {
  interfaces: new Map<string, Interface>()
};

const interfaceReducerCreator = createReducer(
  initialState,
  on(loadInterfacesSuccess, (state, { interfaces }) => {
    const interfaceMap: Map<string, Interface> = new Map<string, Interface>(
      interfaces.map(myInterface => [myInterface.id, myInterface])
    );
    return update(state, { interfaces: { $set: interfaceMap } });
  })
);

export function interfaceReducer(
  state: InterfaceState | undefined,
  action: Action
) {
  return interfaceReducerCreator(state, action);
}
