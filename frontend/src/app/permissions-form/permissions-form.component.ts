import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ServiceStore} from '../service-store.service';
import {Service} from '../models/service';

@Component({
  selector: 'app-permissions-form',
  templateUrl: './permissions-form.component.html',
  providers: [ServiceStore]
})
export class PermissionsFormComponent implements OnInit {
  error: string;
  service: Service;
  targetUser: string;

  view: boolean;
  viewPrereleases: boolean;
  edit: boolean;

  constructor(private router: Router, private serviceStore: ServiceStore, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params['serviceId']) {
        this.serviceStore.getService(params['serviceId']).subscribe(
          (element: Service) => {
            this.service = element;
            // TODO: check what permission already exist on this object, use to set the initial checkbox states
            this.view = false;
            this.viewPrereleases = false;
            this.edit = false;
          },
      error => this.error = error.error.userMessage
        );
      }
    });
  }

  public async chmodSpecification() {
    if (!this.targetUser) {
      this.error = 'Username must not be blank';
      return;
    }

    this.serviceStore.chmodSpecification(this.service.id, this.targetUser, this.view, this.viewPrereleases, this.edit)
      .subscribe(event => {
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.userMessage
      );
  }

}
