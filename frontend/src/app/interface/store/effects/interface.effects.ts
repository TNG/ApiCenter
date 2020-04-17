import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { showErrorMessage } from '../../../store/actions/error.actions';
import { InterfaceService } from '../../services/interface.service';
import {
  createInterface,
  createInterfaceSuccess,
  deleteInterface,
  deleteInterfaceSuccess,
  loadInterface,
  loadInterfaces,
  loadInterfacesSuccess,
  loadInterfaceSuccess,
  updateInterface,
  updateInterfaceSuccess
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

  loadApplication$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadInterface),
      mergeMap(({ id }) => {
        return this.interfaceService.getInterface(id).pipe(
          map(myInterface => loadInterfaceSuccess({ interface: myInterface })),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error loading interface. Please try again.'
              })
            )
          )
        );
      })
    )
  );

  createInterface$ = createEffect(() =>
    this.actions$.pipe(
      ofType(createInterface),
      mergeMap(({ interface: myInterface }) => {
        return this.interfaceService.createInterface(myInterface).pipe(
          map(() => createInterfaceSuccess()),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error creating interface. Please try again.'
              })
            )
          )
        );
      })
    )
  );

  createInterfaceSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(createInterfaceSuccess),
      mergeMap(() => of(loadInterfaces()))
    )
  );

  updateInterface$ = createEffect(() =>
    this.actions$.pipe(
      ofType(updateInterface),
      mergeMap(({ interface: myInterface }) => {
        return this.interfaceService.updateInterface(myInterface).pipe(
          map(() => updateInterfaceSuccess()),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error updating interface. Please try again.'
              })
            )
          )
        );
      })
    )
  );

  updateInterfaceSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(updateInterfaceSuccess),
      mergeMap(() => of(loadInterfaces()))
    )
  );

  deleteApplication$ = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteInterface),
      mergeMap(({ interface: myInterface }) => {
        return this.interfaceService.deleteInterface(myInterface.id).pipe(
          map(() => deleteInterfaceSuccess()),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error deleting interface. Please try again.'
              })
            )
          )
        );
      })
    )
  );

  deleteInterfaceSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteInterfaceSuccess),
      mergeMap(() => of(loadInterfaces()))
    )
  );
}
