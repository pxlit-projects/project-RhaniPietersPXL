import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DraftPostListComponent } from './draft-post-list.component';

describe('DraftPostListComponent', () => {
  let component: DraftPostListComponent;
  let fixture: ComponentFixture<DraftPostListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DraftPostListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DraftPostListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
