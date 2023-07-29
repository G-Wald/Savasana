// cypress/integration/login.spec.ts

describe('Login Form', () => {
  beforeEach(() => {
    cy.visit('/login'); 
  });

  it('should display the login form', () => {
    cy.get('.login-form').should('be.visible');
  });

  it('should show an dissabled button on form submission with invalid data', () => {
    cy.get('[formcontrolname="email"]').type('invalidemail');
    cy.get('[formcontrolname="password"]').type('short'); 
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('should login unsuccessfully with invalid credentials', () => {
    const invalidEmail = 'yoga@studio.com';
    const invalidPassword = 'strongpassword';

    cy.get('[formcontrolname="email"]').type(invalidEmail);
    cy.get('[formcontrolname="password"]').type(invalidPassword);
    cy.get('button[type="submit"]').click();

    cy.get('.error').should('be.visible').and('contain.text', 'An error occurred');
  });

  it('should login successfully with valid credentials', () => {
    const validEmail = 'wald@gmail.com';
      const validPassword = 'waldgmail';

    cy.get('[formcontrolname="email"]').type(validEmail);
    cy.get('[formcontrolname="password"]').type(validPassword);
    cy.get('button[type="submit"]').click();
    cy.wait(500); 
    cy.get('.login-form').should('not.exist');
    cy.location('pathname').should('eq', '/sessions');

    cy.get('span').contains('Account').should('exist');
    cy.get('span').contains('Logout').should('exist');
    cy.get('span').contains('Logout').click();
    cy.location('pathname').should('eq', '/');
  });
});
