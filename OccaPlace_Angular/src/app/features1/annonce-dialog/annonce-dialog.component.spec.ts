import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnonceDialogComponent } from './annonce-dialog.component';

describe('AnnonceDialogComponent', () => {
  let component: AnnonceDialogComponent;
  let fixture: ComponentFixture<AnnonceDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AnnonceDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnnonceDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
