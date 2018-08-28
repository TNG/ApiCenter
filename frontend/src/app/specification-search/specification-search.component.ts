import {Component, OnInit} from '@angular/core';
import {Specification} from "../models/specification";
import {SpecificationService} from "../specification.service";

@Component({
  selector: 'app-specification-search',
  templateUrl: './specification-search.component.html',
  styleUrls: ['./specification-search.component.css'],
  providers: [SpecificationService]
})
export class SpecificationSearchComponent implements OnInit {
  specifications: Specification[] = [];
  selectedSpecification: Specification[];

  constructor(private specificationService: SpecificationService) {
  }

  ngOnInit() {
  }

  public customSearch(term: string, item: any): boolean {
    return true;
  }

  private loadSpecifications(event: Event) {
    const searchString = (<HTMLInputElement>event.target).value;

    this.specificationService.searchSpecifications(searchString)
      .subscribe((data: Specification[]) => { this.specifications = data; console.log(data); });
  }

}
