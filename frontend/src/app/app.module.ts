import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { HttpClientModule } from '@angular/common/http';
import { ApplicationModule } from './application/application.module';
import { MaterialModule } from './material/material.module';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { environment } from '../environments/environment';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { ErrorEffects } from './store/effects/error.effects';
import { RouterModule } from '@angular/router';
import { appRoutes } from './app.routes';
import { InterfaceModule } from './interface/interface.module';

@NgModule({
  declarations: [AppComponent, HeaderComponent],
  imports: [
    ApplicationModule,
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    EffectsModule.forRoot([ErrorEffects]),
    HttpClientModule,
    InterfaceModule,
    MaterialModule,
    RouterModule.forRoot(appRoutes),
    StoreDevtoolsModule.instrument({
      logOnly: environment.production
    }),
    StoreModule.forRoot({})
  ],
  providers: [],
  exports: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
