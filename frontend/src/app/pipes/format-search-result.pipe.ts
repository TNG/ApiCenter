import {Pipe, PipeTransform} from '@angular/core';

@Pipe({ name: 'formatSearchResult' })
export class FormatSearchResultPipe implements PipeTransform {

  transform(value: string): string {
    return '<p class="small">' + value + '</p>';
  }

}
