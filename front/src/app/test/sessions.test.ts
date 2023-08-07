import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListComponent } from '../features/sessions/components/list/list.component';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from '../services/session.service';
import { SessionApiService } from '../features/sessions/services/session-api.service';
import { Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

class SessionServiceMock {
  
    private isAdmin: boolean;
  
    constructor(isAdmin: boolean) {
      this.isAdmin = isAdmin;
    }
  
    get sessionInformation() {
      return { admin: this.isAdmin }; 
    }
  }

  class SessionApiServiceMock {
    all() {
      return of([
        { id: 1, name: 'Session 1', date: new Date(), description: 'Description 1' },
        { id: 2, name: 'Session 2', date: new Date(), description: 'Description 2' },
      ]);
    }
  }


describe('ListComponent', () => {

  let component: ListComponent;
    let fixture: ComponentFixture<ListComponent>;
    let router: Router;

    const testSessions = [
      {
        id: 1,
        name: 'Yoga',
        description: 'session de test',
        users: [1, 2, 3], 
        date: new Date(),
        createdAt: new Date(),
        updatedAt: new Date(),
        teacher_id: 101
        },
        {
            id: 2,
            name: 'Yoga2',
            description: 'session de test2',
            users: [1, 2, 3,4], 
            date: new Date(),
            createdAt: new Date(),
            updatedAt: new Date(),
            teacher_id: 102
        },
    ];
  
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports:[
          HttpClientModule,
          RouterTestingModule
      ],
        providers:[{ provide: SessionService, useFactory: () => new SessionServiceMock(true) },
          { provide: SessionApiService, useClass: SessionApiServiceMock },],
          schemas: [CUSTOM_ELEMENTS_SCHEMA]
      }).compileComponents();
    });
  
    beforeEach(() => {
      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      router = TestBed.inject(Router); 
      fixture.detectChanges();
    });

  it('should display sessions', () => {
    component.sessions$ = of(testSessions);

    fixture.detectChanges();
    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionElements.length).toBe(2);
  });

  it('should display create button for admin users', () => {
    component.sessions$ = of(testSessions);
    
    fixture.detectChanges();
    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(createButton).toBeTruthy();

  });

  it('should display update button for admin users', () => {
    component.sessions$ = of(testSessions);
    fixture.detectChanges();
    const updateButton = fixture.nativeElement.querySelector('[ng-reflect-router-link="update,1"]');
    expect(updateButton).toBeTruthy();
  });
  
  it('should not display create button for non-admin users', () => {
    component.sessions$ = of(testSessions);
    fixture.detectChanges();

    const detailButton = fixture.nativeElement.querySelector('[ng-reflect-router-link="detail,1"]');
    expect(detailButton).toBeTruthy();
    });
});

describe('ListComponentWithoutAdminRights', () => {
    let component: ListComponent;
    let fixture: ComponentFixture<ListComponent>;
    let router: Router;
    const testSessions = [
      {
        id: 1,
        name: 'Yoga',
        description: 'session de test',
        users: [1, 2, 3], 
        date: new Date(),
        createdAt: new Date(),
        updatedAt: new Date(),
        teacher_id: 101
        },
        {
            id: 2,
            name: 'Yoga2',
            description: 'session de test2',
            users: [1, 2, 3,4], 
            date: new Date(),
            createdAt: new Date(),
            updatedAt: new Date(),
            teacher_id: 102
        },
    ];
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports:[
          HttpClientModule,
          RouterTestingModule
      ],
        providers:[{ provide: SessionService, useFactory: () => new SessionServiceMock(false) },
          { provide: SessionApiService, useClass: SessionApiServiceMock },],
          schemas: [CUSTOM_ELEMENTS_SCHEMA]
      }).compileComponents();
    });
  
    beforeEach(() => {
      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      router = TestBed.inject(Router); 
      fixture.detectChanges();
    });
  
      
    it('should display sessions', (done) => {
    component.sessions$ = of(testSessions);

      fixture.whenStable().then(() => { 
        const sessionElements = fixture.nativeElement.querySelectorAll('.item');
        expect(sessionElements.length).toBe(2);
        done(); 
      });
    });
  
    it('should not display create button for admin users', () => {
    component.sessions$ = of(testSessions);

      fixture.detectChanges();
      const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
      expect(createButton).toBeFalsy();
    });
    
    it('should display detail button for non-admin users', () => {
      component.sessions$ = of(testSessions);
      fixture.detectChanges();
      const detailButton = fixture.nativeElement.querySelector('[ng-reflect-router-link="detail,1"]');
      expect(detailButton).toBeTruthy();
      });

    it('should not display create update for non-admin users', () => {
      component.sessions$ = of(testSessions);
      fixture.detectChanges();
      const updateButton = fixture.nativeElement.querySelector('[ng-reflect-router-link="update,1"]');
      expect(updateButton).toBeFalsy();
    });
});
