import {Pipe, PipeTransform} from "@angular/core";

@Pipe({ name: 'trimSearchResult' })
export class TrimSearchResultPipe implements PipeTransform {

  transform(value: string, searchString: string): string {
    let returnString = "";

    const startingPositions = this.getAllStartingPositions(value, searchString);

    for (let i = 0; i <= startingPositions.length; i++) {
      let startingPosition = startingPositions[i];

      const cutFrom = startingPosition > 20 ? startingPosition - 20: 0;
      const cutTo = startingPosition + searchString.length + 20;

      returnString += value.substring(cutFrom, cutTo) + " ... ";
    }

    return returnString;
  }

  private getAllStartingPositions(value: string, searchString: string): number[] {
    const regex = new RegExp(searchString, "gi");
    let result;
    let startingPositions = [];

    while(result = regex.exec(value)) {
      startingPositions.push(result.index);
    }

    return startingPositions;
  }

}
