import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let service: AuthService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    service = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should send a POST request with correct URL and body for register()', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      password: 'testpassword',
      firstName:"firestname",
      lastName:"lastName"
    };
    const expectedURL = 'api/auth/register';

    service.register(registerRequest).subscribe(() => {

    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(registerRequest); 
    req.flush(null); 
  });

  it('should send a POST request with correct URL and body for login()', () => {
    const loginRequest: LoginRequest = {
      email: 'g@gmail.com',
      password: 'testpassword'
    };
    const expectedURL = 'api/auth/login';

    service.login(loginRequest).subscribe((sessionInformation: SessionInformation) => {
      expect(sessionInformation).toBeTruthy();
    });

    const req = httpTestingController.expectOne(expectedURL);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(loginRequest); 
    req.flush({ token: 'testtoken', userId: '123' }); 
  });
});
