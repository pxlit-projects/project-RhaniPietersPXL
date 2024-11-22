import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DraftDetailComponent } from './draft-detail.component';

describe('DraftDetailComponent', () => {
  let component: DraftDetailComponent;
  let fixture: ComponentFixture<DraftDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DraftDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DraftDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
