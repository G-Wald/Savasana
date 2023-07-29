
describe('Detail Component', () => {
    
    it('Vérifier l\'affichage correct des informations de session', () => {
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('wald@gmail.com');
        cy.get('[formcontrolname="password"]').type('waldgmail'); 
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('span.ml1').contains('Detail').click();

        cy.contains('Session Mise').should('be.visible');
        cy.contains('Margot').should('be.visible');
        cy.contains('0 attendees').should('be.visible');
        cy.contains('August 1, 2023').should('be.visible');
        cy.contains('Description:').should('be.visible');
        cy.contains('Description mise à jour de la session').should('be.visible');
        cy.contains('Create at: July 29, 2023').should('be.visible');
        cy.contains('Last update: July 29, 2023').should('be.visible');
    });
    
    it('should participate', () => {
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('test@example.com');
        cy.get('[formcontrolname="password"]').type('strongpassword'); 
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('span.ml1').contains('Detail').click();

        cy.contains('button', 'Participate').should('be.visible').click();
        cy.contains('button', 'Do not participate').should('be.visible');
    });
    
    it('should unparticipate', () => {
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('test@example.com');
        cy.get('[formcontrolname="password"]').type('strongpassword'); 
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('span.ml1').contains('Detail').click();
        
    
        cy.contains('button', 'Do not participate').should('be.visible').click();
        cy.contains('button', 'Participate').should('be.visible');
    });
    

    it('should not have delete button when no admin', () => {
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('test@example.com');
        cy.get('[formcontrolname="password"]').type('strongpassword'); 
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('span.ml1').contains('Detail').click();

        cy.contains('button', 'Delete').should('not.exist');
    });

    it('should have delete button', () => {
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('wald@gmail.com');
        cy.get('[formcontrolname="password"]').type('waldgmail'); 
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('span.ml1').contains('Detail').click();
    
        cy.contains('button', 'Delete').should('be.visible').click();
        cy.on('window:confirm', () => true);
        cy.location('pathname').should('eq', '/sessions');
        cy.get('.mat-simple-snackbar').should('be.visible').contains('Session deleted !');
    });
    });