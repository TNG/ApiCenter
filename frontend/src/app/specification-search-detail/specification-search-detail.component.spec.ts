import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpecificationSearchDetailComponent } from './specification-search-detail.component';

describe('SpecificationSearchDetailComponent', () => {
  let component: SpecificationSearchDetailComponent;
  let fixture: ComponentFixture<SpecificationSearchDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpecificationSearchDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpecificationSearchDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
