import {FormatSearchResultPipe} from './format-search-result.pipe';

describe('FormatSearchResultPipe', () => {
  let formatSearchResultPipe: FormatSearchResultPipe;

  beforeEach(() => {
    formatSearchResultPipe = new FormatSearchResultPipe();
  });

  it('should format standard string', () => {
    expect(formatSearchResultPipe.transform('test string')).toBe('<p class="small">test string</p>');
  });
});
