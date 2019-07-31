import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ServiceStore} from '../service-store.service';
import {Role} from '../models/role';

@Component({
  selector: 'app-permissions-form',
  templateUrl: './permissions-form.component.html',
  providers: [ServiceStore]
})
export class PermissionsFormComponent implements OnInit {
  error: string;
  targetUser = '';
  serviceId: string;
  role: Role;
  keys = Object.keys;
  allRoles = Role;

  constructor(private router: Router,
              private serviceStore: ServiceStore,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.serviceId = params['serviceId'];
    });
  }

  public async changePermissionsForService() {
    if (this.targetUser === '') {
      this.error = 'Enter a username';
      return;
    }

    this.serviceStore.changeRoleForService(this.serviceId, this.targetUser, this.role)
      .subscribe(event => {
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.userMessage
      );
  }

  public selectorOnChange() {
    if (this.serviceId) {
      this.serviceStore.getRoleForService(this.serviceId, this.targetUser).subscribe(
        (element: Role) => {
          this.role = element;
        },
        error => this.error = error.error.userMessage
      );
    }
  }
}
