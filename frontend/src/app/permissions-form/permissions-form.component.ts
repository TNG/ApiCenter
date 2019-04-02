import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SpecificationFile, SpecificationMetaData} from '../models/specificationfile';
import {SpecificationService} from '../specification.service';
import {Specification} from '../models/specification';
import {ApiLanguage} from '../models/version';

@Component({
  selector: 'app-permissions-form',
  templateUrl: './permissions-form.component.html',
  providers: [SpecificationService]
})
export class PermissionsFormComponent implements OnInit {
  error: string;
  specification: Specification;
  targetUser: string;
  grantRead: boolean;
  grantWrite: boolean;

  constructor(private router: Router, private specificationService: SpecificationService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.specificationService.getSpecification(params['id']).subscribe((specification: Specification) => {
        this.specification = specification;
        // Ideally, check what permission already exist on this object and set the initial state of the checkboxes
        // Need an extra GET query, with this target user
        this.grantRead = false;
        this.grantWrite = false;
      });
    });
  }

  public async chmodSpecification(id, targetUserId, grantRead, grantWrite) {
    if (!targetUserId) {
      this.error = 'Username must not be blank';
      return;
    }

    this.specificationService.chmodSpecification(id, targetUserId, grantRead, grantWrite)
      .subscribe(event => {
          this.router.navigateByUrl('/');
        },
        error => this.error = error.error.userMessage
        // eg. current user doesn't have permission to modify permissions
        // eg. this spec doesn't have a long castable id
        // eg. target user doesn't exist
      );
  }

}
