import {Component, OnInit} from '@angular/core';
import {Specification} from "../models/specification";
import {SpecificationService} from "../specification.service";

@Component({
  selector: 'app-specification-search-detail',
  templateUrl: './specification-search-detail.component.html',
  styleUrls: ['./specification-search-detail.component.css'],
  providers: [SpecificationService]
})
export class SpecificationSearchDetailComponent implements OnInit {
  specifications: Specification[];
  searchString: string;

  constructor(private specificationService: SpecificationService) {
  }

  ngOnInit() {
  }

  public searchSpecifications(event) {
    if (event.keyCode == 13) {
      this.specificationService.searchSpecifications(this.searchString).subscribe((data: Specification[]) => this.specifications = data);
    }
  }

}
