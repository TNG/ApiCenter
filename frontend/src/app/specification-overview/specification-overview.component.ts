import {Component, OnInit} from '@angular/core';
import {SpecificationService} from '../specification.service';
import {Specification} from '../models/specification';
import {VersionService} from '../version.service';

@Component({
  selector: 'app-specification-overview',
  templateUrl: './specification-overview.component.html',
  styleUrls: ['./specification-overview.component.css'],
  providers: [SpecificationService, VersionService]
})
export class SpecificationOverviewComponent implements OnInit {
  specifications: Specification[];
  error: String;
  expanded: String[] = [];

  constructor(private specificationService: SpecificationService, private versionService: VersionService) {
  }

  ngOnInit(): void {
    this.getSpecifications();
  }

  public async deleteSpecification(specification) {
    if (confirm('Are you sure that you want to delete "' + specification.title + '"?')) {
      this.specificationService.deleteSpecification(specification.id)
        .subscribe(event => this.getSpecifications());
    }
  }

  public async deleteVersion(version) {
    if (confirm('Are you sure that you want to delete version "' + version.version + '"?')) {
      this.versionService.deleteVersion(version.id).subscribe(event => {
          this.getSpecifications();
          this.expanded = [];
        }
      );
    }
  }

  public async synchronize(specification) {
    return this.specificationService.synchronizeSpecification(specification.id)
      .subscribe(event => this.getSpecifications());
  }

  public async switchExpanded(specification) {
    if (this.expanded.includes(specification.id)) {
      this.expanded.splice(this.expanded.indexOf(specification.id), 1);
    } else {
      this.expanded.push(specification.id);
    }
  }

  private async getSpecifications() {
    this.specificationService.getSpecifications().subscribe((data: Specification[]) => this.specifications = data);
  }
}
