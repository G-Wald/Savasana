import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterComponent } from '../features/auth/components/register/register.component';
import { AuthService } from '../features/auth/services/auth.service';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: any;
  let routerMock: any;

  beforeEach(async () => {
    authServiceMock = {
      register: jest.fn(),
      login: jest.fn(),
    };

    routerMock = {
      navigate: jest.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });
  
  it('should disable the submit button when the form is invalid', () => {
    component.form.controls['email'].setValue('');
    component.form.controls['password'].setValue('');
    component.form.controls['firstName'].setValue('');
    component.form.controls['lastName'].setValue('');
    expect(component.form.valid).toBeFalsy();
    fixture.detectChanges();
  
    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBeTruthy();

    component.form.controls['email'].setValue('bryan@gmail.com');
    component.form.controls['password'].setValue('bryanPassword');
    component.form.controls['firstName'].setValue('');
    component.form.controls['lastName'].setValue('inthekitchen');
    expect(component.form.valid).toBeFalsy();
    fixture.detectChanges();
    expect(submitButton.disabled).toBeTruthy();

    component.form.controls['email'].setValue('bryan@gmail.com');
    component.form.controls['password'].setValue('bryanPassword');
    component.form.controls['firstName'].setValue('bryan');
    component.form.controls['lastName'].setValue('');
    expect(component.form.valid).toBeFalsy();
    fixture.detectChanges();
    expect(submitButton.disabled).toBeTruthy();

    component.form.controls['email'].setValue('bryan@gmail.com');
    component.form.controls['password'].setValue('');
    component.form.controls['firstName'].setValue('bryan');
    component.form.controls['lastName'].setValue('inthekitchen');
    expect(component.form.valid).toBeFalsy();
    fixture.detectChanges();
    expect(submitButton.disabled).toBeTruthy();

    component.form.controls['email'].setValue('bryangmail');
    component.form.controls['password'].setValue('bryanPassword');
    component.form.controls['firstName'].setValue('bryan');
    component.form.controls['lastName'].setValue('inthekitchen');
    expect(component.form.valid).toBeFalsy();
    fixture.detectChanges();
    expect(submitButton.disabled).toBeTruthy();

    component.form.controls['email'].setValue('bryan@gmail.com');
    component.form.controls['password'].setValue('bryanPassword');
    component.form.controls['firstName'].setValue('bryan');
    component.form.controls['lastName'].setValue('inthekitchen');
    expect(component.form.valid).toBeTruthy();
    fixture.detectChanges();
    expect(submitButton.disabled).toBeFalsy();
  });

  it('should register successfully and navigate to /login', () => {
    const registerRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'securePassword',
    };

    // Configurer le mock pour que la fonction register() du service d'authentification retourne une valeur observable
    authServiceMock.register.mockReturnValue(of(void 0));

    // Remplir le formulaire avec des informations de connexion valides
    component.form.setValue(registerRequest);

    // Déclencher la soumission du formulaire
    component.submit();

    // Mettre à jour la vue pour refléter les changements
    fixture.detectChanges();

    // Vérifier que le Router a été appelé avec la bonne URL
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should display an error message on registration failure', () => {
    // Configurer le mock pour que la fonction register() du service d'authentification retourne une erreur
    authServiceMock.register.mockReturnValue(throwError({}));

    // Remplir le formulaire avec des informations de connexion valides
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'securePassword',
    });

    // Déclencher la soumission du formulaire
    component.submit();

    // Mettre à jour la vue pour refléter les changements
    fixture.detectChanges();

    // Vérifier que le message d'erreur est affiché
    const errorMessage = fixture.nativeElement.querySelector('.error');
    expect(errorMessage.textContent).toContain('An error occurred');
  });

  it('should call authService.register on form submission', () => {
    // Remplir le formulaire avec des informations de connexion valides
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'securePassword',
    });

    // Configurer le mock pour que la fonction register() du service d'authentification retourne une valeur observable
    authServiceMock.register.mockReturnValue(of(void 0));

    // Déclencher la soumission du formulaire
    component.submit();

    // Vérifier que la méthode register() du service d'authentification a été appelée avec les bonnes valeurs
    expect(authServiceMock.register).toHaveBeenCalledWith({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'securePassword',
    });
  });
});
