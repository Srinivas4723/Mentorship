import { ComponentFixture, TestBed } from '@angular/core/testing';
import { faL } from '@fortawesome/free-solid-svg-icons';

import { CompensationuserComponent } from './compensationuser.component';

describe('CompensationuserComponent', () => {
  let component: CompensationuserComponent;
  let fixture: ComponentFixture<CompensationuserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompensationuserComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompensationuserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('isFormValid',()=>{
    expect(false);
  })
});
