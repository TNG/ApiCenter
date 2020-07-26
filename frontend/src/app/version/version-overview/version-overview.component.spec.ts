import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VersionOverviewComponent } from './version-overview.component';

describe('VersionOverviewComponent', () => {
  let component: VersionOverviewComponent;
  let fixture: ComponentFixture<VersionOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [VersionOverviewComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VersionOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
