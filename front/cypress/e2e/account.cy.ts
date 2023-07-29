// cypress/integration/login.spec.ts

describe('Account', () => {
    
    it('Vérifier l\'affichage correct des informations utilisateur', () => {
        cy.visit('/login'); // Replace with the URL of your login page
        cy.get('[formcontrolname="email"]').type('wald@gmail.com');
        cy.get('[formcontrolname="password"]').type('waldgmail');
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('span').contains('Account').should('exist');
        cy.get('span').contains('Account').click();
        cy.location('pathname').should('eq', '/me');

        // Vérifiez que le titre "User information" est affiché correctement.
        cy.contains('h1', 'User information').should('be.visible');
    
        // Vérifiez que le nom complet de l'utilisateur est affiché correctement.
        cy.contains('Name: coucou VERO').should('be.visible');
    
        // Vérifiez que l'adresse e-mail de l'utilisateur est affichée correctement.
        cy.contains('Email: wald@gmail.com').should('be.visible');
    
        // Vérifiez que le message "You are admin" est affiché si l'utilisateur est un administrateur.
        cy.contains('You are admin').should('exist'); // Remarque : Le message "You are admin" ne doit pas être visible car l'utilisateur n'est pas administrateur dans l'exemple fourni.
    
        // Vérifiez que le bouton "Delete my account" est affiché si l'utilisateur n'est pas administrateur.
        cy.contains('button', 'Detail').should('not.exist');
    
        // Vérifiez que les dates de création et de dernière mise à jour sont affichées correctement.
        cy.contains('Create at: July 29, 2023').should('be.visible');
        cy.contains('Last update: July 29, 2023').should('be.visible');
      });
    
      it('should create an account and delete it', () => {

        cy.visit('/register')
        const validFirstName = 'test';
        const validLastName = 'test2';
        const validEmail = 'test@suppressioncompte.com';
        const validPassword = 'strongpassword';
  
        cy.get('[formcontrolname="firstName"]').type(validFirstName);
        cy.get('[formcontrolname="lastName"]').type(validLastName);
        cy.get('[formcontrolname="email"]').type(validEmail);
        cy.get('[formcontrolname="password"]').type(validPassword);
        cy.get('button[type="submit"]').click();
        cy.wait(500)
        cy.get('[formcontrolname="email"]').type(validEmail);
        cy.get('[formcontrolname="password"]').type(validPassword);
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('span').contains('Account').should('exist');
        cy.get('span').contains('Account').click();
        cy.location('pathname').should('eq', '/me');
    
        cy.contains('button', 'Detail').should('be.visible');
        cy.contains('button', 'Detail').click();
        cy.on('window:confirm', () => true);
        cy.wait(500); 
        cy.location('pathname').should('eq', '/');
        cy.get('.mat-simple-snackbar').should('be.visible').contains('Your account has been deleted !');
      });
  });