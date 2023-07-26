/**
* @jest-environment jsdom
*/
import { expect } from '@jest/globals';
import { LoginComponent } from '../features/auth/components/login/login.component';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { of, throwError } from 'rxjs';
import { AuthService } from '../features/auth/services/auth.service';

  describe('LoginComponent', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let router: Router;
    let authServiceMock: any; 

    beforeEach(async () => {
      authServiceMock = {
        login: jest.fn(),
      };
      await TestBed.configureTestingModule({
        declarations: [LoginComponent],
        imports: [ 
        RouterTestingModule.withRoutes([]),
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule     
        ],
        providers: [
          { provide: AuthService, useValue: authServiceMock }, // Utiliser le mock du service d'authentification
        ],
      }).compileComponents();
    });
  
    beforeEach(() => {
      fixture = TestBed.createComponent(LoginComponent);
      component = fixture.componentInstance;
      router = TestBed.inject(Router);
      const routerSpy = jest.spyOn(router, 'navigate');
      router.initialNavigation();
      fixture.detectChanges();
    });
  
    it('should create the component', () => {
      expect(component).toBeTruthy();
    });
    
    it('should disable the submit button when the form is invalid', () => {
      component.form.controls['email'].setValue('');
      component.form.controls['password'].setValue('');
      expect(component.form.valid).toBeFalsy();
      fixture.detectChanges();
    
      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBeTruthy();
    });
    
    it('should display an error for incorrect email', () => {
      component.form.controls['email'].setValue('qzedgvf,fgq');
      component.form.controls['password'].setValue('wrongpassword');
      expect(component.form.valid).toBeFalsy();
      fixture.detectChanges();
    
      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBeTruthy();
    });

    it('should display an error message for incorrect email and password', () => {
      component.form.controls['email'].setValue('incorrect@example.com');
      component.form.controls['password'].setValue('wrongpassword');
      fixture.detectChanges();
    
      authServiceMock.login.mockReturnValue(throwError({}));
      component.submit();
      fixture.detectChanges();
    
      const errorMessage = fixture.nativeElement.querySelector('.error');
      expect(errorMessage).toBeTruthy(); // Vérifie que le message d'erreur est affiché
      expect(errorMessage.textContent).toContain('An error occurred'); // Vérifie le contenu du message d'erreur
    });
    

    it('should enable the submit button when the form is valid', () => {
      component.form.controls['email'].setValue('test@example.com');
      component.form.controls['password'].setValue('password123');
      expect(component.form.valid).toBeTruthy();
      fixture.detectChanges();
    
      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBeFalsy();
    });


    it('should log in successfully and redirect to /sessions', async () => {
      // Remplir le formulaire avec des informations de connexion valides
      component.form.controls['email'].setValue('test@example.com');
      component.form.controls['password'].setValue('securePassword');
  
      // Configurer le mock pour que la fonction login() du service d'authentification retourne une valeur observable
      const mockSessionInformation: SessionInformation = {admin: true, firstName:"bryan", lastName:"inthekitchen",type: "Anglais", username:"bryan@gmail.com", id:1 , token:"token" };
      authServiceMock.login.mockReturnValue(of(mockSessionInformation));
  
      // Déclencher la soumission du formulaire
      component.submit();
  
      // Mettre à jour la vue pour refléter les changements
      fixture.detectChanges();
  
      // Vérifier que le message d'erreur n'est pas affiché
      const errorMessage = fixture.nativeElement.querySelector('.error');
      expect(errorMessage).toBeFalsy();
  
      // Vérifier que le router a été appelé avec la bonne URL
      expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should call authService.register on form submission', () => {
      // Remplir le formulaire avec des informations de connexion valides
      component.form.setValue({
        email: 'test@example.com',
        password: 'securePassword',
      });
  
      // Configurer le mock pour que la fonction register() du service d'authentification retourne une valeur observable
      authServiceMock.login.mockReturnValue(of(void 0));
  
      // Déclencher la soumission du formulaire
      component.submit();
  
      // Vérifier que la méthode register() du service d'authentification a été appelée avec les bonnes valeurs
      expect(authServiceMock.login).toHaveBeenCalledWith({
        email: 'test@example.com',
        password: 'securePassword',
      });
    });
  });

  