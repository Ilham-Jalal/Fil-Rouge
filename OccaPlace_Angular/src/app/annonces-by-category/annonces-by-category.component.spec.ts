import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnoncesByCategoryComponent } from './annonces-by-category.component';

describe('AnnoncesByCategoryComponent', () => {
  let component: AnnoncesByCategoryComponent;
  let fixture: ComponentFixture<AnnoncesByCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AnnoncesByCategoryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnnoncesByCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
