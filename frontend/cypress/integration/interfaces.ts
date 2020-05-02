describe('Interfaces', () => {
  it('should create, show, update and delete an interface', () => {
    cy.visit('/interfaces');

    // create
    cy.contains('add').click();

    cy.get('form')
      .findByLabelText(/application/i)
      .click();
    cy.get('mat-option')
      .contains('Application C')
      .click();

    cy.get('form')
      .findByLabelText(/name/i)
      .type('New interface');
    cy.get('form')
      .findByLabelText(/description/i)
      .type('New interface description');

    cy.get('form')
      .findByLabelText(/type/i)
      .click();
    cy.get('mat-option')
      .contains('GRAPHQL')
      .click();

    cy.get('form')
      .contains('Save')
      .click();

    // check if shown in table
    cy.get('table')
      .contains('td', 'New interface')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'GRAPHQL')
      .should('be.visible');

    // check if shown on details page
    cy.get('table')
      .contains('td', 'New interface')
      .click();

    cy.get('table')
      .contains('td', 'New interface')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'New interface description')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'GRAPHQL')
      .should('be.visible');

    // check if application data is shown on details page
    cy.get('table')
      .contains('td', 'Application C')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'This is the third application')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'John Doe, john.doe@company.com')
      .should('be.visible');

    // check if interface is shown in application details page
    cy.get('mat-toolbar')
      .contains('button', 'Applications')
      .click();
    cy.get('table')
      .contains('td', 'Application C')
      .click();

    cy.get('table')
      .contains('td', 'New interface')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'GRAPHQL')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'New interface')
      .click();

    // update interface
    cy.findByText('Edit').click();

    cy.get('form')
      .findByLabelText(/application/i)
      .click();
    cy.get('mat-option')
      .contains('Application B')
      .click();

    cy.get('form')
      .findByLabelText(/name/i)
      .clear()
      .type('Updated interface');
    cy.get('form')
      .findByLabelText(/description/i)
      .clear()
      .type('Updated interface description');

    cy.get('form')
      .findByLabelText(/type/i)
      .click();
    cy.get('mat-option')
      .contains('REST')
      .click();

    cy.get('form')
      .contains('Save')
      .click();

    cy.get('table')
      .contains('td', 'Updated interface')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'REST')
      .should('be.visible');

    cy.get('mat-toolbar')
      .contains('button', 'Applications')
      .click();
    cy.get('table')
      .contains('td', 'Application B')
      .click();

    cy.get('table')
      .contains('td', 'Updated interface')
      .click();

    // delete interface
    cy.findByText('Delete').click();
    cy.get('mat-dialog-container')
      .findByText('Delete')
      .click();

    cy.findByText('Updated interface').should('not.exist');
  });
});
