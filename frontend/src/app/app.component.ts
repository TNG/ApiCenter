import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AppState } from './store/state/app.state';
import { Store } from '@ngrx/store';
import { filter, takeUntil } from 'rxjs/operators';
import { selectErrorMessage } from './store/selectors/error.selectors';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  private unsubscribe$ = new Subject();

  constructor(private store: Store<AppState>, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.store
      .select(selectErrorMessage)
      .pipe(
        takeUntil(this.unsubscribe$),
        filter(errorMessage => !!errorMessage)
      )
      .subscribe(errorMessage =>
        this.snackBar.open(errorMessage, 'close', { duration: 3000 })
      );
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }
}
