import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLivreurDialogComponent } from './add-livreur-dialog.component';

describe('AddLivreurDialogComponent', () => {
  let component: AddLivreurDialogComponent;
  let fixture: ComponentFixture<AddLivreurDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddLivreurDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddLivreurDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
