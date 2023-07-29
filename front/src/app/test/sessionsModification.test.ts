import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { FormComponent } from '../features/sessions/components/form/form.component';
import { SessionApiService } from '../features/sessions/services/session-api.service';
import { TeacherService } from '../services/teacher.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiServiceMock: Partial<SessionApiService>;
  let teacherServiceMock: Partial<TeacherService>;
  let routerMock: Partial<Router>;
  let matSnackBarMock: Partial<MatSnackBar>;

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

      matSnackBarMock = {
        open: jest.fn()
      };

      TestBed.configureTestingModule({
        declarations: [FormComponent],
        providers: [
          FormBuilder,
          { provide: SessionApiService, useValue: sessionApiServiceMock },
          { provide: TeacherService, useValue: teacherServiceMock },
          { provide: Router, useValue: routerMock },
          { provide: MatSnackBar, useValue: matSnackBarMock },
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { paramMap: { get: jest.fn() } } }
          }
        ]
      }).compileComponents();

      fixture = TestBed.createComponent(FormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    })
  );

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('initForm', () => {
    it('should initialize the form without session data', () => {
        component['initForm'](); 
      expect(component.sessionForm).toBeTruthy();
      expect(component.sessionForm?.get('name')?.value).toBe('');
      expect(component.sessionForm?.get('date')?.value).toBe('');
      expect(component.sessionForm?.get('teacher_id')?.value).toBe('');
      expect(component.sessionForm?.get('description')?.value).toBe('');
    });

    it('should initialize the form with session data', () => {
      const sessionData = {
        id: '1',
        name: 'Test Session',
        date: new Date().toISOString(),
        description: 'Test Description',
        teacher_id: '101',
      };
      component['initForm'](); 
      expect(component.sessionForm).toBeTruthy();
      expect(component.sessionForm?.get('name')?.value).toBe(sessionData.name);
      expect(component.sessionForm?.get('date')?.value).toBe(sessionData.date.split('T')[0]);
      expect(component.sessionForm?.get('teacher_id')?.value).toBe(sessionData.teacher_id);
      expect(component.sessionForm?.get('description')?.value).toBe(sessionData.description);
    });
  });

  describe('exitPage', () => {
    it('should show a snackbar message and navigate to /sessions', () => {
      const message = 'Session created !';
      spyOn(component as any, 'exitPage');
      // Simulate form submission
        component.submit();
      expect(matSnackBarMock.open).toHaveBeenCalledWith(message, 'Close', { duration: 3000 });
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('submit', () => {
    it('should call SessionApiService.create() when onSubmit is false', () => {
      component.onUpdate = false;
      component.sessionForm = new FormBuilder().group({
        name: new FormControl('Test Session', Validators.required),
        date: new FormControl('2023-07-28', Validators.required),
        teacher_id: new FormControl('teacher-id', Validators.required),
        description: new FormControl('Test Description', [
          Validators.required,
          Validators.max(2000)
        ]),
      });

      component.submit();

      expect(sessionApiServiceMock.create).toHaveBeenCalled();
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should call SessionApiService.update() when onSubmit is true', () => {
      const sessionData = {
        id: '1',
        name: 'Test Session',
        date: new Date().toISOString(),
        description: 'Test Description',
        teacher_id: '101',
      };
      component.onUpdate = true;
      component.sessionForm = new FormBuilder().group({
        name: new FormControl('Updated Session', Validators.required),
        date: new FormControl(sessionData.date.split('T')[0], Validators.required),
        teacher_id: new FormControl('updated-teacher-id', Validators.required),
        description: new FormControl('Updated Description', [
          Validators.required,
          Validators.max(2000)
        ]),
      });

      component.submit();

      expect(sessionApiServiceMock.update).toHaveBeenCalledWith('1', {
        id: '1',
        name: 'Updated Session',
        date: '2023-07-28',
        teacher_id: 'updated-teacher-id',
        description: 'Updated Description'
      });
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });
});
