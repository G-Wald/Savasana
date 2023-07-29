import '@cypress/code-coverage/support';
export {}
declare global {
    namespace Cypress {
        interface Chainable {
            login(email:string, password:string): Chainable<void>;
        }
    }
}