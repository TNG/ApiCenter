import { ApplicationState } from '../../application/store/state/application.state';
import { ErrorState } from './error.state';

export interface AppState {
  applicationState: ApplicationState;
  errorState: ErrorState;
}
