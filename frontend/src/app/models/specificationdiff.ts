export class Endpoint {
  pathUrl: string;
  method: string;
  summary: string;

  constructor(
    pathUrl: string,
    method: string,
    summary: string,
  ) {
    this.pathUrl = pathUrl;
    this.method = method;
    this.summary = summary;
  }
}

export class SpecificationDiff {
  deprecatedEndpoints: Endpoint[];
  diff: boolean;
  diffBackwardsCompatible: boolean;
  missingEndpoints: Endpoint[];
  newEndpoints: Endpoint[];

  constructor(
    deprecatedEndpoints: Endpoint[],
    diff: boolean,
    diffBackwardsCompatible: boolean,
    missingEndpoints: Endpoint[],
    newEndpoints: Endpoint[],
  ) {
    this.deprecatedEndpoints = deprecatedEndpoints;
    this.diff = diff;
    this.diffBackwardsCompatible = diffBackwardsCompatible;
    this.missingEndpoints = missingEndpoints;
    this.newEndpoints = newEndpoints;
  }
}
