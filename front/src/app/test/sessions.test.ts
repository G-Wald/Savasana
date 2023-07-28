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

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports:[
        HttpClientModule,
        RouterTestingModule
    ],
      providers:[{ provide: SessionService, useFactory: () => new SessionServiceMock(true) },
        { provide: SessionApiService, useClass: SessionApiServiceMock },]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router); 
    fixture.detectChanges();
  });

  it('should display empty list when sessions$ is empty', () => {
    component.sessions$ = of([]);
  
    fixture.detectChanges();
    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionElements.length).toBe(0);
  });
  

  it('should display sessions', () => {
    component.sessions$ = of([
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
    ]);

    fixture.detectChanges();
    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionElements.length).toBe(2);
  });

  it('should display create button for admin users', () => {
    fixture.detectChanges();
    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(createButton).toBeTruthy();
  });

  it('should display update button for admin users', () => {
    fixture.whenStable().then(() => {
        const updateButton = fixture.debugElement.query(By.css('button[routerLink^="update"]'));
        expect(updateButton).toBeTruthy();
  
        // Vérifier que routerLink est correct pour le bouton "Detail"
        expect(updateButton.nativeElement.getAttribute('routerLink')).toBe('/update/' + 1);
        }); 
  });
  
  it('should not display create button for non-admin users', () => {
    fixture.whenStable().then(() => {
        const detailButton = fixture.debugElement.query(By.css('button[routerLink^="detail"]'));
        expect(detailButton).toBeTruthy();
  
        // Vérifier que routerLink est correct pour le bouton "Detail"
        expect(detailButton.nativeElement.getAttribute('routerLink')).toBe('/detail/' + 1);
        }); 
    });
});

describe('ListComponentWithoutAdminRights', () => {
    let component: ListComponent;
    let fixture: ComponentFixture<ListComponent>;
    let router: Router;
  
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports:[
          HttpClientModule,
          RouterTestingModule
      ],
        providers:[{ provide: SessionService, useFactory: () => new SessionServiceMock(false) },
          { provide: SessionApiService, useClass: SessionApiServiceMock },]
      }).compileComponents();
    });
  
    beforeEach(() => {
      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      router = TestBed.inject(Router); 
      fixture.detectChanges();
    });
  
      
    it('should display sessions', () => {
      component.sessions$ = of([
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
      ]);
  
      fixture.detectChanges();
      const sessionElements = fixture.nativeElement.querySelectorAll('.item');
      expect(sessionElements.length).toBe(2);
    });
  
    it('should display create button for admin users', () => {
      fixture.detectChanges();
      const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
      expect(createButton).toBeFalsy();
    });
    
    it('should not display create button for non-admin users', () => {
      fixture.whenStable().then(() => {
          const detailButton = fixture.debugElement.query(By.css('button[routerLink^="detail"]'));
          expect(detailButton).toBeTruthy();
    
          // Vérifier que routerLink est correct pour le bouton "Detail"
          expect(detailButton.nativeElement.getAttribute('routerLink')).toBe('/detail/' + 1);
          }); 
      });

    it('should not display create button for non-admin users', () => {
    fixture.whenStable().then(() => {
        const detailButton = fixture.debugElement.query(By.css('button[routerLink^="update"]'));
        expect(detailButton).toBeFalsy();
        }); 
    });
});
