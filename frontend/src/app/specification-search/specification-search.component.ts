import {Component, OnInit} from '@angular/core';
import {ServiceStore} from '../service-store.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-specification-search',
  templateUrl: './specification-search.component.html',
  styleUrls: ['./specification-search.component.css'],
  providers: [ServiceStore]
})
export class SpecificationSearchComponent implements OnInit {
  searchString: string;

  constructor(private serviceStore: ServiceStore, private router: Router) {
  }

  ngOnInit() {
  }

  public searchForString(event, searchHelp) {
    if (event.keyCode === 13) {
      if (this.searchString !== undefined && this.searchString !== '') {
        searchHelp.close();
        this.router.navigate(['search', this.searchString]);
        this.searchString = '';
      } else {
        searchHelp.open();
      }
    }
  }
}
