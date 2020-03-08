import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
  createApplication,
  createApplicationFailure,
  createApplicationSuccess,
  deleteApplication,
  deleteApplicationFailure,
  deleteApplicationSuccess,
  loadApplications,
  loadApplicationsFailure,
  loadApplicationsSuccess
} from '../actions/application.actions';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { ApplicationService } from '../../../services/application.service';
import { of } from 'rxjs';

@Injectable()
export class ApplicationEffects {
  constructor(
    private actions$: Actions,
    private applicationService: ApplicationService
  ) {}

  loadApplications$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadApplications),
      mergeMap(() => {
        return this.applicationService.getApplications().pipe(
          map(applications => loadApplicationsSuccess({ applications })),
          catchError(() => of(loadApplicationsFailure()))
        );
      })
    )
  );

  createApplication$ = createEffect(() =>
    this.actions$.pipe(
      ofType(createApplication),
      mergeMap(({ application }) => {
        return this.applicationService.createApplication(application).pipe(
          map(() => createApplicationSuccess()),
          catchError(() => of(createApplicationFailure()))
        );
      })
    )
  );

  createApplicationSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(createApplicationSuccess),
      mergeMap(() => of(loadApplications()))
    )
  );

  deleteApplication$ = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteApplication),
      mergeMap(({ application }) => {
        return this.applicationService.deleteApplication(application).pipe(
          map(() => deleteApplicationSuccess()),
          catchError(() => of(deleteApplicationFailure()))
        );
      })
    )
  );

  deleteApplicationSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteApplicationSuccess),
      mergeMap(() => of(loadApplications()))
    )
  );
}
