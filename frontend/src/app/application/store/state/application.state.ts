import { Application } from '../../../models/application';

export interface ApplicationState {
  applications: Map<string, Application>;
}
