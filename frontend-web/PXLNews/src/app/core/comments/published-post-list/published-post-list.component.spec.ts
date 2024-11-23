import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublishedPostListComponent } from './published-post-list.component';

describe('PublishedPostListComponent', () => {
  let component: PublishedPostListComponent;
  let fixture: ComponentFixture<PublishedPostListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublishedPostListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublishedPostListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
