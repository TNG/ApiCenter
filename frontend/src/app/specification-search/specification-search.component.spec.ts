import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpecificationSearchComponent } from './specification-search.component';

describe('SpecificationSearchComponent', () => {
  let component: SpecificationSearchComponent;
  let fixture: ComponentFixture<SpecificationSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpecificationSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpecificationSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
