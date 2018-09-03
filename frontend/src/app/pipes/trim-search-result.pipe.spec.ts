import {TrimSearchResultPipe} from './trim-search-result.pipe';

describe('TrimSearchResultPipe', () => {
  let trimSearchResultPipe: TrimSearchResultPipe;
  const searchString = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis pulvinar laoreet lorem, in massa nunc';

  beforeEach(() => {
    trimSearchResultPipe = new TrimSearchResultPipe();
  });

  it('should trim string on both sides', () => {
    expect(trimSearchResultPipe.transform(searchString, 'elit'))
      .toBe(' dolor sit amet, consectetur adipiscing elit. Duis pulvinar laoreet lorem, in massa \u2026');
  });

  it('should trim when word occurs at beginning', () => {
    expect(trimSearchResultPipe.transform(searchString, 'Lorem'))
      .toBe('Lorem ipsum dolor sit amet, consectetur adipi\u2026 adipiscing elit. Duis pulvinar laoreet lorem, in massa nunc\u2026');
  });

  it('should trim when word occurs at end', () => {
    expect(trimSearchResultPipe.transform(searchString, 'nunc'))
      .toBe('. Duis pulvinar laoreet lorem, in massa nunc\u2026');
  });
});
