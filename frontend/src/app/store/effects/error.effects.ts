import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { showErrorMessage } from '../actions/error.actions';
import { mergeMap } from 'rxjs/operators';

@Injectable()
export class ErrorEffects {
  constructor(private actions$: Actions, private snackBar: MatSnackBar) {}

  showErrorMessage$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(showErrorMessage),
        mergeMap(async ({ errorMessage }) => {
          this.snackBar.open(errorMessage, 'close', { duration: 3000 });
        })
      ),
    { dispatch: false }
  );
}
