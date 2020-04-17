import { InterfaceType } from './interfaceType';

export interface Interface {
  id?: string;
  name: string;
  description?: string;
  type: InterfaceType;
  applicationId: string;
}
