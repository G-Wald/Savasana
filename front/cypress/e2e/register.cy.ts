describe('Registration Form', () => {
    beforeEach(() => {
      cy.visit('/register'); // Replace with the URL of your registration page
    });
  
    it('should display the registration form', () => {
      cy.get('.register-form').should('be.visible');
    });
  
    it('should show an error message on form submission with invalid data', () => {
      cy.get('[formcontrolname="firstName"]').type(' '); 
      cy.get('[formcontrolname="lastName"]').type(' '); 
      cy.get('[formcontrolname="email"]').type('invalidemail');
      cy.get('[formcontrolname="password"]').type('short'); 
      cy.get('button[type="submit"]').should('be.disabled');
    });
  
    it('should register successfully with valid credentials', () => {
      // Replace these with valid test data for registration
      const validFirstName = 'Alan';
      const validLastName = 'Coben';
      const validEmail = 'test@example2.com';
      const validPassword = 'strongpassword';
  
      cy.get('[formcontrolname="firstName"]').type(validFirstName);
      cy.get('[formcontrolname="lastName"]').type(validLastName);
      cy.get('[formcontrolname="email"]').type(validEmail);
      cy.get('[formcontrolname="password"]').type(validPassword);
      cy.get('button[type="submit"]').click();
  
      // Assert that the registration was successful (you can check for a logged-in state or a specific page after registration)
      // For example, you can check if the registration form is no longer visible, or if a welcome message is displayed.
      cy.get('.login-form').should('exist');
    });
  });