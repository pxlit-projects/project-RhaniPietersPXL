import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalPostListComponent } from './approval-post-list.component';

describe('ApprovalPostListComponent', () => {
  let component: ApprovalPostListComponent;
  let fixture: ComponentFixture<ApprovalPostListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApprovalPostListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApprovalPostListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
