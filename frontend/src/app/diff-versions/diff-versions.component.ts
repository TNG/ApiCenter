import {Component, OnInit, ViewChild} from '@angular/core';
import {ServiceStore} from '../service-store.service';
import {Service} from '../models/service';
import {SpecificationStore} from '../specification-store.service';
import {ActivatedRoute} from '@angular/router';
import {SpecificationDiff} from '../models/specificationdiff';

@Component({
  selector: 'app-diff-versions',
  templateUrl: './diff-versions.component.html',
  styleUrls: ['./diff-versions.component.css'],
  providers: [ServiceStore, SpecificationStore]
})
export class DiffVersionsComponent implements OnInit {
  service: Service;
  error: string;

  radioSelectedBeforeVersion;
  radioSelectedAfterVersion;
  searchComplete = false;
  diff: SpecificationDiff;
  validRangeSelected = false;
  noEndpointChanges = true;

  @ViewChild('tabs')
  private tabs: any;


  constructor(private serviceStore: ServiceStore,
              private specificationStore: SpecificationStore,
              private route: ActivatedRoute,
              ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['serviceId']) {
        this.getService(params['serviceId']);
      }
    });
  }

  private async getService(serviceId: string) {
    this.serviceStore.getService(serviceId).subscribe(
     (element: Service) => {
       this.service = new Service(element.id, element.title, element.description, element.specifications, element.remoteAddress);
       this.service.sortVersionsSemantically();
       this.searchComplete = true;
     },
     error1 => {
       if (error1.status === 403) {
         this.error = 'You don\'t have permission to access content on this page';
       }
     }
   );
  }

  public radioButtonChange(event) {
    const versions = [this.radioSelectedBeforeVersion, this.radioSelectedAfterVersion];
    if (versions[0] !== undefined && versions[1] !== undefined && versions[0] !== versions[1]) {

      this.specificationStore.getSpecificationDiff(this.service.id, versions[0], versions[1])
        .subscribe((data: SpecificationDiff) => {
          this.validRangeSelected = true;

          this.diff = new SpecificationDiff(
            data.deprecatedEndpoints,
            data.diff,
            data.diffBackwardsCompatible,
            data.missingEndpoints,
            data.newEndpoints
          );

          const isTabDisabled = [this.diff.newEndpoints, this.diff.missingEndpoints, this.diff.deprecatedEndpoints]
            .map(endpoints => endpoints.length === 0);

          this.noEndpointChanges = isTabDisabled.every(value => value);

          // We want to display the first tab to contain endpoints
          if (isTabDisabled[0] && isTabDisabled[1]) {
            this.tabs.activeId = 'deprecated';
          } else if (isTabDisabled[0]) {
            this.tabs.activeId = 'missing';
          } else {
            this.tabs.activeId = 'new';
          }

      });

    } else {
      this.validRangeSelected = false;
    }
  }
}
