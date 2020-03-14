import { Action, createReducer, on } from '@ngrx/store';
import update from 'immutability-helper';
import { ErrorState } from '../state/error.state';
import { clearErrorMessage, setErrorMessage } from '../actions/error.actions';

const initialState: ErrorState = {
  errorMessage: undefined
};

const errorReducerCreator = createReducer(
  initialState,
  on(setErrorMessage, (state, { errorMessage }) =>
    update(state, { errorMessage: { $set: errorMessage } })
  ),
  on(clearErrorMessage, state =>
    update(state, { errorMessage: { $set: undefined } })
  )
);

export function errorReducer(state: ErrorState | undefined, action: Action) {
  return errorReducerCreator(state, action);
}
