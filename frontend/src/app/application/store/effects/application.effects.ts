import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
  loadApplications,
  loadApplicationsFailure,
  loadApplicationsSuccess
} from '../actions/application.actions';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { ApplicationService } from '../../../services/application.service';
import { Application } from '../../../models/application';
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
          map((applications: Application[]) =>
            loadApplicationsSuccess({ applications })
          ),
          catchError(() => loadApplicationsFailure)
        );
      })
    )
  );
}
