import { AppState } from '../state/app.state';
import { createSelector } from '@ngrx/store';

const selectErrorState = (state: AppState) => state.errorState;

export const selectErrorMessage = createSelector(
  selectErrorState,
  state => state.errorMessage
);
