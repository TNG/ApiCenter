import {HighlightSearchResultPipe} from './highlight-search-result.pipe';

describe('HighlightSearchResultPipe', () => {
  let highlightSearchResultPipe: HighlightSearchResultPipe;

  beforeEach(() => {
    highlightSearchResultPipe = new HighlightSearchResultPipe();
  });

  it('should hightlight words in text', () => {
    expect(highlightSearchResultPipe.transform('this word should be highlighted', 'word'))
      .toBe('this <strong>word</strong> should be highlighted');
  });

  it('should highlight nothing when no words in text', () => {
    expect(highlightSearchResultPipe.transform('nothing should be highlighted', 'word'))
      .toBe('nothing should be highlighted');
  });
});
