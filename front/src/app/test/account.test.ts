import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { MeComponent } from '../components/me/me.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { User } from '../interfaces/user.interface';
import { DetailComponent } from '../features/sessions/components/detail/detail.component';
import { SessionApiService } from '../features/sessions/services/session-api.service';
import { TeacherService } from '../services/teacher.service';
import { expect } from '@jest/globals';
import { Router } from '@angular/router';
import { Session } from '../features/sessions/interfaces/session.interface';
import { Teacher } from '../interfaces/teacher.interface';
import { FormBuilder } from '@angular/forms';
import { SessionService } from '../services/session.service';
import { UserService } from '../services/user.service';
import { Overlay } from '@angular/cdk/overlay'; 
import { HttpClientModule } from '@angular/common/http';
import { By } from '@angular/platform-browser';

describe('MeComponent Integration Tests', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let overlay: Overlay;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let testSession: Session;
  let testTeacher: Teacher;
  let router: Router;

  class UserServiceMock {
    getById(userId: string) {
      const user: User = {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        admin: false,
        password: "tutu",
        createdAt: new Date(),
        updatedAt: new Date(),
      };

      return of(user);
    }

    delete(userId: string) {
      return of({});
    }
  }

  class SessionServiceMock {
    sessionInformation: { id: number } | null = { id: 1 }; // Mocked sessionInformation
    logOut=jest.fn();
  }
  
  class RouterMock {
    navigate = jest.fn();
  }

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [MeComponent],
      providers: [
        MatSnackBar,
        { provide: UserService, useClass: UserServiceMock },
        { provide: SessionService, useClass: SessionServiceMock },
        { provide: Router, useClass: RouterMock },
        Overlay,
      ],
      imports: [RouterTestingModule.withRoutes([]),
        HttpClientModule]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MeComponent);
    mockSessionApiService = TestBed.inject(SessionApiService) as jest.Mocked<SessionApiService>;
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    overlay = TestBed.inject(Overlay); 
    router = TestBed.inject(Router); 
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should display user information', () => {

    jest.spyOn(userService, 'getById').mockReturnValue(of({
        id: 1,
      firstName: 'Alan',
      lastName: 'Coben',
      email: 'tutu@example.com',
      admin: true,
      password: "tutu",
      createdAt: new Date(),
      updatedAt: new Date(),
      }));
       

    component.ngOnInit();
    
    fixture.detectChanges();

    const paragraphElements = fixture.nativeElement.querySelectorAll('p');
    let isadmin = false;
    paragraphElements.forEach((paragraph: HTMLElement) => {
      
        const paragraphText = paragraph.textContent;
        if(paragraphText != null){
            if (paragraphText.includes('Name:')) {
                expect(paragraphText).toContain('Alan'); 
                expect(paragraphText).toContain('COBEN'); 
            }
            if (paragraphText.includes('Email:')) {
                expect(paragraphText).toContain('tutu@example.com'); 
            }
            if (paragraphText.includes('You are admin')) {
                isadmin = true;
            }
        }
    });
    expect(isadmin).toBe(true); 
    const deleteButton = fixture.debugElement.query(By.css('button[mat-raised-button][color="warn"]'));
    expect(component.user?.admin).toBe(true);
    expect(deleteButton).toBeFalsy();
  });

  it('should display delete button if user is not admin', () => {
    const user: User = {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      admin: false,
      password: "tutu",
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    component.user = user;
    fixture.detectChanges();

    const paragraphElements = fixture.nativeElement.querySelectorAll('p');
    let isadmin = false;
    paragraphElements.forEach((paragraph: HTMLElement) => {
        const paragraphText = paragraph.textContent;
        if(paragraphText != null){
            if (paragraphText.includes('You are admin')) {
                isadmin = true;
            }
        }
    });
    expect(isadmin).toBe(false); 

    const deleteButton = fixture.debugElement.query(By.css('button[mat-raised-button][color="warn"]'));
    expect(deleteButton).toBeTruthy();
  });

  it('should call UserService delete method and navigate to home', () => {

    const mockUser: User = { id: 1, firstName: 'Alan', lastName: 'Coben', email: 'alan.coben@example.com',password: "pwd", admin: false, createdAt:new Date,updatedAt:new Date };
    jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));
    const deleteSpy = jest.spyOn(userService, 'delete').mockReturnValue(of({}));
    const snackBarOpenSpy = jest.spyOn(matSnackBar, 'open');
    const sessionLogOutSpy = jest.spyOn(sessionService, 'logOut');


    fixture.detectChanges(); 

    const deleteButton = fixture.debugElement.query(By.css('button[mat-raised-button][color="warn"]'));
    deleteButton.nativeElement.click();

    expect(deleteSpy).toHaveBeenCalledWith('1'); 
    expect(snackBarOpenSpy).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
  });
});
