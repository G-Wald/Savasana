import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';
import { expect } from '@jest/globals';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });

    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should send a GET request with correct URL for getById()', () => {
    const userId = '1';
    const expectedURL = `api/user/${userId}`;

    service.getById(userId).subscribe((user: User) => {
      expect(user).toBeTruthy();
      expect(user.id).toBe(userId);
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('GET');
    req.flush({ id: userId, name: 'User 1' });
  });

  it('should send a DELETE request with correct URL for delete()', () => {
    const userId = '1';
    const expectedURL = `api/user/${userId}`;

    service.delete(userId).subscribe(() => {
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('DELETE');
    req.flush(null); 
  });
});
