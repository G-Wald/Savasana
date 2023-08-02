import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    service = TestBed.inject(TeacherService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  
  it('should send a GET request with correct URL for all()', () => {
    const expectedURL = 'api/teacher';

    service.all().subscribe((teachers: Teacher[]) => {
      expect(teachers).toBeTruthy();
      expect(teachers.length).toBeGreaterThan(0);
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('GET');
    req.flush([{ id: '1', name: 'Teacher 1' }, { id: '2', name: 'Teacher 2' }]);
  });

  it('should send a GET request with correct URL for detail()', () => {
    const teacherId = '1';
    const expectedURL = `api/teacher/${teacherId}`;

    service.detail(teacherId).subscribe((teacher: Teacher) => {
      expect(teacher).toBeTruthy();
      expect(teacher.id).toBe(teacherId);
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('GET');
    req.flush({ id: teacherId, name: 'Teacher 1' });
  });

});
