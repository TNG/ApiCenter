import {Pipe, PipeTransform} from "@angular/core";

@Pipe({ name: 'highlightSearchResult' })
export class HighlightSearchResultPipe implements PipeTransform {

  transform(value: string, searchString: string): string {
    return value.replace(new RegExp(searchString, "gi"), match => {
      return '<strong>' + match + '</strong>';
    });
  }

}
