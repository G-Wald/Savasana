// cypress/integration/login.spec.ts

describe('Login Form', () => {
  beforeEach(() => {
    cy.visit('/login'); // Replace with the URL of your login page
  });

  it('should display the login form', () => {
    cy.get('.login-form').should('be.visible');
  });

  it('should show an dissabled button on form submission with invalid data', () => {
    cy.get('[formcontrolname="email"]').type('invalidemail');
    cy.get('[formcontrolname="password"]').type('short'); // Replace with an invalid password
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('should login unsuccessfully with invalid credentials', () => {
    // Replace these with your valid email and password for testing
    const invalidEmail = 'yoga@studio.com';
    const invalidPassword = 'strongpassword';

    cy.get('[formcontrolname="email"]').type(invalidEmail);
    cy.get('[formcontrolname="password"]').type(invalidPassword);
    cy.get('button[type="submit"]').click();

    cy.get('.error').should('be.visible').and('contain.text', 'An error occurred');
  });

  it('should login successfully with valid credentials', () => {
    // Replace these with your valid email and password for testing
    const validEmail = 'wald@gmail.com';
      const validPassword = 'waldgmail';

    cy.get('[formcontrolname="email"]').type(validEmail);
    cy.get('[formcontrolname="password"]').type(validPassword);
    cy.get('button[type="submit"]').click();

    cy.get('.login-form').should('not.exist');
    cy.location('pathname').should('eq', '/sessions');
  });
});
