import { HttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TokenStorageService } from '../token-storage.service';
import { UserService } from '../user.service';

import { ReportuserComponent } from './reportuser.component';

describe('ReportuserComponent', () => {
  let component: ReportuserComponent;
  let fixture: ComponentFixture<ReportuserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports:[HttpClient],
      providers:[UserService,TokenStorageService],
      declarations: [ ReportuserComponent , HttpClient]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportuserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
