import { createAction, props } from '@ngrx/store';

export const showErrorMessage = createAction(
  'Set error message',
  props<{ errorMessage: string }>()
);
export const clearErrorMessage = createAction('Clear error message');
