describe('Registration Form', () => {
    beforeEach(() => {
      cy.visit('/register'); 
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
      
      const validFirstName = 'Alan';
      const validLastName = 'Coben';
      const validEmail = 'test@example2.com';
      const validPassword = 'strongpassword';
  
      cy.get('[formcontrolname="firstName"]').type(validFirstName);
      cy.get('[formcontrolname="lastName"]').type(validLastName);
      cy.get('[formcontrolname="email"]').type(validEmail);
      cy.get('[formcontrolname="password"]').type(validPassword);
      cy.get('button[type="submit"]').click();
      cy.get('.login-form').should('exist');
    });
    after(() => {
      cy.visit('/login'); 
      cy.get('[formcontrolname="email"]').type('test@example2.com');
        cy.get('[formcontrolname="password"]').type('strongpassword');
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('span').contains('Account').should('exist');
        cy.get('span').contains('Account').click();
        cy.location('pathname').should('eq', '/me');
    
        cy.contains('button', 'Detail').should('be.visible');
        cy.contains('button', 'Detail').click();
        cy.on('window:confirm', () => true);
    });
  });