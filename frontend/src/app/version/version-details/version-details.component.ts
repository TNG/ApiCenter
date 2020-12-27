import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { filter, map, switchMap, tap } from 'rxjs/operators';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { AppState } from '../../store/state/app.state';
import { Store } from '@ngrx/store';
import { selectVersion } from '../store/selectors/version.selectors';
import { Version } from '../../models/version';
import { MatDialog } from '@angular/material/dialog';
import { VersionDeleteComponent } from '../version-delete/version-delete.component';
import { loadVersion } from '../store/actions/version.actions';

@Component({
  selector: 'app-version-details',
  templateUrl: './version-details.component.html',
  styleUrls: ['./version-details.component.scss']
})
export class VersionDetailsComponent implements OnInit {
  version$: Observable<Version>;
  spec$: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private store: Store<AppState>,
    private matDialog: MatDialog
  ) {}

  ngOnInit() {
    this.version$ = this.activatedRoute.paramMap.pipe(
      tap(params => {
        this.store.dispatch(loadVersion({ id: params.get('id') }));
      }),
      switchMap((params: ParamMap) =>
        this.store.select(selectVersion, { id: params.get('id') })
      )
    );
    this.spec$ = this.version$.pipe(
      filter(version => !!version),
      map(version => JSON.parse(version.content))
    );
  }

  onClickDelete(version: Version) {
    this.matDialog.open(VersionDeleteComponent, {
      data: { version }
    });
  }
}
