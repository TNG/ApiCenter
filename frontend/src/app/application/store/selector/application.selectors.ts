import { ApplicationState } from '../state/application.state';

export const selectApplications = (state: ApplicationState) =>
  state.applications;
