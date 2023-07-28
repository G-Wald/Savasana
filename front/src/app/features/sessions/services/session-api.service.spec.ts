import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should send a GET request with correct URL for all()', () => {
    const expectedURL = 'api/session';

    service.all().subscribe((sessions: Session[]) => {
      expect(sessions).toBeTruthy();
      expect(sessions.length).toBeGreaterThan(0);
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('GET');
    req.flush([{ id: '1', name: 'Session 1' }, { id: '2', name: 'Session 2' }]);
  });

  it('should send a GET request with correct URL for detail()', () => {
    const sessionId = '1';
    const expectedURL = `api/session/${sessionId}`;

    service.detail(sessionId).subscribe((session: Session) => {
      expect(session).toBeTruthy();
      expect(session.id).toBe(sessionId);
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('GET');
    req.flush({ id: sessionId, name: 'Session 1' });
  });

  it('should send a DELETE request with correct URL for delete()', () => {
    const sessionId = '1';
    const expectedURL = `api/session/${sessionId}`;

    service.delete(sessionId).subscribe(() => {
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('DELETE');
    req.flush(null); 
  });

  it('should send a POST request with correct URL and body for create()', () => {
    const sessionToCreate: Session = {
      id: 1,
      name: 'Test Session',
      date: new Date(),
      description: 'Test Description',
      teacher_id:101,
      users:[1,2,3],
      createdAt:new Date,
      updatedAt: new Date
    };

    const expectedURL = 'api/session';

    service.create(sessionToCreate).subscribe((createdSession: Session) => {
      expect(createdSession).toBeTruthy();
      expect(createdSession.id).toBe(sessionToCreate.id);
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(sessionToCreate); 
    req.flush(sessionToCreate); 
  });

  it('should send a PUT request with correct URL and body for update()', () => {
    const sessionId = "1";
    const updatedSession: Session = {
      id: 1,
      name: 'Updated Session',
      date: new Date(),
      description: 'Updated Description',
      teacher_id:101,
      users:[1,2,3],
      createdAt:new Date,
      updatedAt: new Date
    };

    const expectedURL = `api/session/${sessionId}`;

    service.update(sessionId, updatedSession).subscribe((updatedResult: Session) => {
      expect(updatedResult).toBeTruthy();
      expect(updatedResult.name).toBe(updatedSession.name);
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession); 
    req.flush(updatedSession); 
  });

  it('should send a POST request with correct URL and body for participate()', () => {
    const sessionId = '1';
    const userId = '123';

    const expectedURL = `api/session/${sessionId}/participate/${userId}`;

    service.participate(sessionId, userId).subscribe(() => {
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull(); 
    req.flush(null); 
  });

  it('should send a DELETE request with correct URL for unParticipate()', () => {
    const sessionId = '1';
    const userId = '123';

    const expectedURL = `api/session/${sessionId}/participate/${userId}`;

    service.unParticipate(sessionId, userId).subscribe(() => {
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('DELETE');
    req.flush(null); 
  });

});
