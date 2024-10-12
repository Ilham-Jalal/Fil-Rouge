import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateAnnonceDialogComponent } from './update-annonce-dialog.component';

describe('UpdateAnnonceDialogComponent', () => {
  let component: UpdateAnnonceDialogComponent;
  let fixture: ComponentFixture<UpdateAnnonceDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdateAnnonceDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateAnnonceDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
