import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { AppState } from '../../store/state/app.state';
import { Store } from '@ngrx/store';
import { selectVersion } from '../store/selectors/version.selectors';
import { Version } from '../../models/version';

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
    private store: Store<AppState>
  ) {}

  ngOnInit() {
    this.version$ = this.activatedRoute.paramMap.pipe(
      switchMap((params: ParamMap) =>
        this.store.select(selectVersion, { id: params.get('id') })
      )
    );
    this.spec$ = this.version$.pipe(
      map(version => JSON.parse(version.content))
    );
  }
}