import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { loadInterfacesSuccess } from '../../../interface/store/actions/interface.actions';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { showErrorMessage } from '../../../store/actions/error.actions';
import { loadVersions, loadVersionsSuccess } from '../actions/version.actions';
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
}
