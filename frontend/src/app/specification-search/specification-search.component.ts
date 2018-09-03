import {Component, OnInit} from '@angular/core';
import {SpecificationService} from '../specification.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-specification-search',
  templateUrl: './specification-search.component.html',
  styleUrls: ['./specification-search.component.css'],
  providers: [SpecificationService]
})
export class SpecificationSearchComponent implements OnInit {
  searchString: string;

  constructor(private specificationService: SpecificationService, private router: Router) {
  }

  ngOnInit() {
  }

  public searchForString(event) {
    if (event.keyCode === 13) {
      this.router.navigate(['search', this.searchString]);
      this.searchString = '';
    }
  }


}
