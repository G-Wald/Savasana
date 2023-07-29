describe('Registration Form', () => {
    
    it('Afficher la liste des sessions', () => {
        
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('wald@gmail.com');
        cy.get('[formcontrolname="password"]').type('waldgmail'); 
        cy.get('button[type="submit"]').click();
        cy.get('.list mat-card-title').should('contain', 'Rentals available');
        cy.get('.item').should('have.length.greaterThan', 0);
      });
      
  
      it('Vérifier le bouton "Create" pour un utilisateur admin', () => {
        
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('wald@gmail.com');
        cy.get('[formcontrolname="password"]').type('waldgmail'); 
        cy.get('button[type="submit"]').click();
        cy.get('button[routerLink="create"]').should('be.visible');
        cy.get('button[routerLink="create"]').click();
        cy.location('pathname').should('eq', '/sessions/create');
      });
  
      it('Vérifier le bouton "Edit" pour un utilisateur admin sur une session spécifique', () => {
        
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('wald@gmail.com');
        cy.get('[formcontrolname="password"]').type('waldgmail'); 
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('span.ml1').contains('Edit').should('be.visible');
        cy.get('span.ml1').contains('Edit').click();
        cy.location('pathname').should('eq', '/sessions/update/1');

      });

      it('Vérifier la navigation vers la page de détails', () => {
        
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('wald@gmail.com');
        cy.get('[formcontrolname="password"]').type('waldgmail'); 
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('span.ml1').contains('Detail').should('be.visible');
        cy.get('span.ml1').contains('Detail').click();
        cy.location('pathname').should('eq', '/sessions/detail/1');
      
      });

      it('Vérifier que le bouton "Create" et Update n\'est pas visible pour un utilisateur non-admin', () => {
        
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('test@example.com');
        cy.get('[formcontrolname="password"]').type('strongpassword'); 
        cy.get('button[type="submit"]').click();
        cy.wait(500); 
        cy.get('button[routerLink="create"]').should('not.exist');
        cy.get('span.ml1').contains('Edit').should('not.exist');
      });
  });
 