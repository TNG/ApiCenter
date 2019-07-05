import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ServiceStore} from '../service-store.service';
import {Permissions} from '../models/permissions';

@Component({
  selector: 'app-permissions-form',
  templateUrl: './permissions-form.component.html',
  providers: [ServiceStore]
})
export class PermissionsFormComponent implements OnInit {
  error: string;
  targetUser = '';
  serviceId: string;
  permissions: Permissions = {
    view: false,
    viewPrereleases: false,
    edit: false
  };

  constructor(private router: Router,
              private serviceStore: ServiceStore,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.serviceId = params['serviceId'];
    });
  }

  public async chmodSpecification() {
    this.serviceStore.chmodService(this.serviceId, this.targetUser, this.permissions)
      .subscribe(event => {
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.userMessage
      );
  }

  public updateCheckboxes() {
    if (this.serviceId) {
      this.serviceStore.getmodService(this.serviceId, this.targetUser).subscribe(
        (element: Permissions) => {
          this.permissions = element;
        },
        error => this.error = error.error.userMessage
      );
    }
  }
}
