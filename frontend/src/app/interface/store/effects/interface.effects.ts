import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { showErrorMessage } from '../../../store/actions/error.actions';
import { InterfaceService } from '../../services/interface.service';
import {
  loadInterfaces,
  loadInterfacesSuccess
} from '../actions/interface.actions';

@Injectable()
export class InterfaceEffects {
  constructor(
    private actions$: Actions,
    private interfaceService: InterfaceService
  ) {}

  loadInterfaces$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadInterfaces),
      mergeMap(() => {
        return this.interfaceService.getInterfaces().pipe(
          map(interfaces => loadInterfacesSuccess({ interfaces })),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error loading interfaces. Please try again.'
              })
            )
          )
        );
      })
    )
  );
}
