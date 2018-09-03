import {Pipe, PipeTransform} from '@angular/core';

@Pipe({ name: 'trimSearchResult' })
export class TrimSearchResultPipe implements PipeTransform {

  transform(value: string, searchString: string): string {
    let returnString = '';

    const startingPositions = this.getAllStartingPositions(value, searchString);

    for (let i = 0; i < startingPositions.length; i++) {
      const startingPosition = startingPositions[i];

      const cutFrom = startingPosition > 40 ? startingPosition - 40 : 0;
      const cutTo = startingPosition + searchString.length + 40;

      returnString += value.substring(cutFrom, cutTo) + '\u2026';
    }

    return returnString;
  }

  private getAllStartingPositions(value: string, searchString: string): number[] {
    const regex = new RegExp(searchString, 'gi');
    let result;
    const startingPositions = [];

    while (result = regex.exec(value)) {
      startingPositions.push(result.index);
    }

    return startingPositions;
  }

}
