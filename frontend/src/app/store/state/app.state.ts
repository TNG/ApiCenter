import { ApplicationState } from '../../application/store/state/application.state';
import { InterfaceState } from '../../interface/store/state/interface.state';
import { VersionState } from '../../version/store/state/version.state';

export interface AppState {
  applicationState: ApplicationState;
  interfaceState: InterfaceState;
  versionState: VersionState;
}
