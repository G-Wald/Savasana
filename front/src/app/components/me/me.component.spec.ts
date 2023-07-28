import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';
import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockRouter: Router;
  let mockSessionService: SessionService;
  let mockMatSnackBar: MatSnackBar;
  let mockUserService: UserService;
  
  beforeEach(async () => {   
    
    const routerSpy = { navigate: jest.fn() };
    const sessionServiceSpy = { sessionInformation: { id: 1 }, logOut: jest.fn() };
    const matSnackBarSpy = { open: jest.fn() };
    const userServiceSpy = { delete: jest.fn(), getById: jest.fn() };
    
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: SessionService, useValue: sessionServiceSpy },
        { provide: MatSnackBar, useValue: matSnackBarSpy },
        { provide: UserService, useValue: userServiceSpy },],
    })
      .compileComponents();
    
      mockRouter = TestBed.inject(Router);
      mockSessionService = TestBed.inject(SessionService);
      mockMatSnackBar = TestBed.inject(MatSnackBar);
      mockUserService = TestBed.inject(UserService);
  
      fixture = TestBed.createComponent(MeComponent);
      component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call delete and logout', () => {
    const dummyUserId = "1";
    const deleteMock = jest.fn().mockReturnValue(of(null));

    mockUserService.delete = deleteMock;

    // Call the delete method
    component.delete();

    expect(mockUserService.delete).toHaveBeenCalledWith(dummyUserId);
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', {
      duration: 3000,
    });
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
