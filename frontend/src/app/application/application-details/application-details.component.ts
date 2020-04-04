import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Application } from '../../models/application';
import { Store } from '@ngrx/store';
import { AppState } from '../../store/state/app.state';
import { selectApplication } from '../store/selector/application.selectors';
import { loadApplication } from '../store/actions/application.actions';

@Component({
  selector: 'app-application-details',
  templateUrl: './application-details.component.html',
  styleUrls: ['./application-details.component.scss']
})
export class ApplicationDetailsComponent implements OnInit {
  private application$: Observable<Application>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private store: Store<AppState>
  ) {}

  ngOnInit() {
    this.application$ = this.activatedRoute.paramMap.pipe(
      tap(params => {
        this.store.dispatch(loadApplication({ id: params.get('id') }));
      }),
      switchMap((params: ParamMap) =>
        this.store.select(selectApplication, { id: params.get('id') })
      )
    );
  }
}
