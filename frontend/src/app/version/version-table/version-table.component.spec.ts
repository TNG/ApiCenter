import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VersionTableComponent } from './version-table.component';

describe('VersionTableComponent', () => {
  let component: VersionTableComponent;
  let fixture: ComponentFixture<VersionTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [VersionTableComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VersionTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
