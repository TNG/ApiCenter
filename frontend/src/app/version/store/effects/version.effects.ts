import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { showErrorMessage } from '../../../store/actions/error.actions';
import {
  createVersion,
  createVersionSuccess,
  deleteVersion,
  deleteVersionSuccess,
  loadVersions,
  loadVersionsSuccess
} from '../actions/version.actions';
import { VersionService } from '../../services/version.service';

@Injectable()
export class VersionEffects {
  constructor(
    private actions$: Actions,
    private versionService: VersionService
  ) {}

  loadVersions$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadVersions),
      mergeMap(() => {
        return this.versionService.getVersions().pipe(
          map(versions => loadVersionsSuccess({ versions })),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error loading versions. Please try again.'
              })
            )
          )
        );
      })
    )
  );

  createVersion$ = createEffect(() =>
    this.actions$.pipe(
      ofType(createVersion),
      mergeMap(({ versionFile }) => {
        return this.versionService.createVersion(versionFile).pipe(
          map(() => createVersionSuccess()),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error creating version. Please try again.'
              })
            )
          )
        );
      })
    )
  );

  createVersionSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(createVersionSuccess),
      mergeMap(() => of(loadVersions()))
    )
  );

  deleteVersion$ = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteVersion),
      mergeMap(({ version }) => {
        return this.versionService.deleteVersion(version.id).pipe(
          map(() => deleteVersionSuccess()),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error deleting version. Please try again.'
              })
            )
          )
        );
      })
    )
  );

  deleteVersionSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteVersionSuccess),
      mergeMap(() => of(loadVersions()))
    )
  );
}
