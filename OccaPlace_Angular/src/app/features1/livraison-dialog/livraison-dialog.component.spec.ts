import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LivraisonDialogComponent } from './livraison-dialog.component';

describe('LivraisonDialogComponent', () => {
  let component: LivraisonDialogComponent;
  let fixture: ComponentFixture<LivraisonDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LivraisonDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LivraisonDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
