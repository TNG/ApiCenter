import {Component, OnInit} from '@angular/core';
import {SpecificationService} from '../specification.service';
import {Specification} from '../models/specification';

@Component({
  selector: 'app-specification-overview',
  templateUrl: './specification-overview.component.html',
  styleUrls: ['./specification-overview.component.css'],
  providers: [SpecificationService]
})
export class SpecificationOverviewComponent implements OnInit {
  specifications: Specification[];
  error: String;

  constructor(private specificationService: SpecificationService) {
  }

  ngOnInit(): void {
    this.getSpecifications();
  }

  public async delete(specification) {
    if (confirm('Are you sure that you want to delete "' + specification.title + '"?')) {
      this.specificationService.deleteSpecification(specification.id)
        .subscribe(event => this.getSpecifications());
    }
  }

  public async synchronize(specification) {
    return this.specificationService.synchronizeSpecification(specification.id)
      .subscribe(event => this.getSpecifications());
  }

  private async getSpecifications() {
    this.specificationService.getSpecifications().subscribe((data: Specification[]) => this.specifications = data);
  }
}
