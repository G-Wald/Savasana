describe('Registration Form', () => {
    
    it('Afficher la liste des sessions', () => {
        //login
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('wald@gmail.com');
        cy.get('[formcontrolname="password"]').type('waldgmail'); // Replace with an invalid password
        cy.get('button[type="submit"]').click();
      
        // Vérifier le titre de la page
        cy.get('.list mat-card-title').should('contain', 'Rentals available');
      
        // Vérifier que les sessions sont affichées
        cy.get('.item').should('have.length.greaterThan', 0);
      });
      
  
      it('Vérifier le bouton "Create" pour un utilisateur admin', () => {
        //login
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('wald@gmail.com');
        cy.get('[formcontrolname="password"]').type('waldgmail'); // Replace with an invalid password
        cy.get('button[type="submit"]').click();

        // Vérifier que le bouton "Create" est visible
        cy.get('button[routerLink="create"]').should('be.visible');
        cy.get('button[routerLink="create"]').click();

        cy.location('pathname').should('eq', '/create');
      });
  
      it('Vérifier le bouton "Edit" pour un utilisateur admin sur une session spécifique', () => {
        // Simuler un utilisateur admin
        //login
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('wald@gmail.com');
        cy.get('[formcontrolname="password"]').type('waldgmail'); // Replace with an invalid password
        cy.get('button[type="submit"]').click();
      
        // Vérifier que le bouton "Edit" est visible
        cy.get('button[routerLink="update"]').should('be.visible');
        cy.get('button[routerLink="update"]').click();
        cy.location('pathname').should('eq', '/update');

      });

      it('Vérifier la navigation vers la page de détails', () => {
        // Visiter la page
        cy.visit('/sessions');
      
        // Cliquer sur le bouton "Detail" de la première session
        cy.get('button[routerLink^="detail"]').first().click();
      
        // Vérifier que l'URL correspond à la page de détails de la session
        cy.url().should('match', "/detail");
      });

      it('Vérifier que le bouton "Create" et Update n\'est pas visible pour un utilisateur non-admin', () => {
        // Définir la propriété "user" sur l'objet global window pour simuler un utilisateur non-admin
        //login
        cy.visit('/login'); 
        cy.get('[formcontrolname="email"]').type('test@example.com');
        cy.get('[formcontrolname="password"]').type('strongpassword'); // Replace with an invalid password
        cy.get('button[type="submit"]').click();
        // Vérifier que le bouton "Create" n'est pas visible
        cy.get('button[routerLink="create"]').should('not.exist');
        cy.get('button[routerLink="update"]').should('not.exist');

      });
      
      
  });
 