import { ApplicationState } from '../../application/store/state/application.state';
import { InterfaceState } from '../../interface/store/state/interface.state';

export interface AppState {
  applicationState: ApplicationState;
  interfaceState: InterfaceState;
}
