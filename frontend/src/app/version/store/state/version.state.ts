import { Version } from '../../../models/version';

export interface VersionState {
  versions: Map<string, Version>;
}
