import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserLivraisonsComponent } from './user-livraisons.component';

describe('UserLivraisonsComponent', () => {
  let component: UserLivraisonsComponent;
  let fixture: ComponentFixture<UserLivraisonsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserLivraisonsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserLivraisonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
