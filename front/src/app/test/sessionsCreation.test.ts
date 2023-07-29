import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { FormComponent } from '../features/sessions/components/form/form.component';
import { SessionApiService } from '../features/sessions/services/session-api.service';
import { TeacherService } from '../services/teacher.service';
import { expect } from '@jest/globals';
import { Session } from '../features/sessions/interfaces/session.interface';
import { SessionService } from '../services/session.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Teacher } from '../interfaces/teacher.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let matSnackBar: MatSnackBar;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockSessionService: Partial<SessionService>;
  let router: Router;
  let testSession: Session;
  let testTeacher: Teacher;
  let sessionApiServiceMock: Partial<SessionApiService>;
  let teacherServiceMock: Partial<TeacherService>;
  let routerMock: Partial<Router>;

  beforeEach(
    waitForAsync(() => {
        sessionApiServiceMock = {
            create: jest.fn(),
            update: jest.fn(),
            detail: jest.fn()
          };
    
          teacherServiceMock = {
            all: jest.fn()
          };
    
          routerMock = {
            navigate: jest.fn(), 
            url: '/sessions/create', 
            createUrlTree: jest.fn(), 
            serializeUrl: jest.fn()
          };
      
          mockSessionService = {
              sessionInformation: {
                id: 1,
                firstName:"toto",
                lastName:"tutu",
                token:"token",
                type:"alchimie",
                username:"edward",
                admin: true 
              }
            };
      
      TestBed.configureTestingModule({
        imports: [RouterTestingModule,
            MatSnackBarModule],
        declarations: [FormComponent],
        providers: [
            FormBuilder,
            MatSnackBar,
            { provide: SessionApiService, useValue: sessionApiServiceMock  },
            { provide: TeacherService, useValue: teacherServiceMock  },
            { provide: FormBuilder },
            { provide: Router, useValue: routerMock },
            {provide: SessionService, useValue: mockSessionService },
            {
                provide: ActivatedRoute,
                useValue: { snapshot: { paramMap: { get: jest.fn() } } }
              }
        ]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    mockSessionApiService = TestBed.inject(SessionApiService) as jest.Mocked<SessionApiService>;
    mockTeacherService = TestBed.inject(TeacherService) as jest.Mocked<TeacherService>;
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    jest.spyOn(router, 'navigate');
    router.initialNavigation();
    jest.spyOn(matSnackBar, 'open').mockReturnValue({} as any);
    fixture.detectChanges();

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form in Create mode', () => {
    expect(component.onUpdate).toBeFalsy();
    expect(component.sessionForm).toBeDefined();
  });

  
  it('should initialize the form in Update mode', () => {
    // Mock the session data returned from the API for the update mode
    const session: Session = {
        id: 1,
        name: 'Test Session',
        date: new Date(),
        description: 'Test Description',
        teacher_id:101,
        users:[1,2,3],
        createdAt:new Date,
        updatedAt: new Date
      };

    (sessionApiServiceMock.detail as jest.Mock).mockReturnValue(of(session));
    (routerMock.url as string) = '/sessions/update/1';

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.onUpdate).toBe(true);
    expect(component.sessionForm).toBeDefined();

    expect(component.sessionForm?.value).toEqual({
      name: session.name,
      teacher_id: session.teacher_id,
      description: session.description
    });
  });

/*
  it('should call SessionApiService.create() when submitting in Create mode', () => {
    const createSessionSpy = mockSessionApiService.create;

    // Set the component in Create mode
    component.onUpdate = false;
    component.sessionForm = TestBed.inject(FormBuilder).group({
      name: ['Test Session', []],
      date: ['2023-07-28', []],
      teacher_id: ['teacher-id', []],
      description: ['Test Description', []]
    });

    // Call the submit method
    component.submit();

    expect(createSessionSpy).toHaveBeenCalled();
    expect(matSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
  });

  it('should call SessionApiService.update() when submitting in Update mode', () => {
    const updateSessionSpy = mockSessionApiService.update;

    // Set the component in Update mode
    component.onUpdate = true;
    component.sessionForm = TestBed.inject(FormBuilder).group({
      name: ['Updated Session', []],
      date: ['2023-07-28', []],
      teacher_id: ['updated-teacher-id', []],
      description: ['Updated Description', []]
    });

    // Call the submit method
    component.submit();

    expect(updateSessionSpy).toHaveBeenCalledWith('1', {
      id: '1',
      name: 'Updated Session',
      date: '2023-07-28',
      teacher_id: 'updated-teacher-id',
      description: 'Updated Description'
    });
    expect(matSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
  });

  it('should display "Create session" title when onUpdate is false', () => {
    // Set onUpdate to false
    component.onUpdate = false;
    fixture.detectChanges();

    const titleElement: HTMLElement = fixture.nativeElement.querySelector('h1');
    expect(titleElement.textContent).toContain('Create session');
  });

  it('should display "Update session" title when onUpdate is true', () => {
    // Set onUpdate to true
    component.onUpdate = true;
    fixture.detectChanges();

    const titleElement: HTMLElement = fixture.nativeElement.querySelector('h1');
    expect(titleElement.textContent).toContain('Update session');
  });*/

});
