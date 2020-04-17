import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
  createApplication,
  createApplicationSuccess,
  deleteApplication,
  deleteApplicationSuccess,
  loadApplication,
  loadApplications,
  loadApplicationsSuccess,
  loadApplicationSuccess,
  updateApplication,
  updateApplicationSuccess
} from '../actions/application.actions';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { showErrorMessage } from '../../../store/actions/error.actions';
import { ApplicationService } from '../../services/application.service';

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
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error loading applications. Please try again.'
              })
            )
          )
        );
      })
    )
  );

  loadApplication$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadApplication),
      mergeMap(({ id }) => {
        return this.applicationService.getApplication(id).pipe(
          map(application => loadApplicationSuccess({ application })),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error loading application. Please try again.'
              })
            )
          )
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
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error creating application. Please try again.'
              })
            )
          )
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

  updateApplication$ = createEffect(() =>
    this.actions$.pipe(
      ofType(updateApplication),
      mergeMap(({ application }) => {
        return this.applicationService.updateApplication(application).pipe(
          map(() => updateApplicationSuccess()),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error updating application. Please try again.'
              })
            )
          )
        );
      })
    )
  );

  updateApplicationSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(updateApplicationSuccess),
      mergeMap(() => of(loadApplications()))
    )
  );

  deleteApplication$ = createEffect(() =>
    this.actions$.pipe(
      ofType(deleteApplication),
      mergeMap(({ application }) => {
        return this.applicationService.deleteApplication(application).pipe(
          map(() => deleteApplicationSuccess()),
          catchError(() =>
            of(
              showErrorMessage({
                errorMessage: 'Error deleting application. Please try again.'
              })
            )
          )
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
