describe('Applications', () => {
  it('should create, show, update and delete a new application', () => {
    cy.visit('/');

    // create
    cy.contains('add').click();

    cy.get('form')
      .findByLabelText(/name/i)
      .type('New application title');
    cy.get('form')
      .findByLabelText(/description/i)
      .type('New application description');
    cy.get('form')
      .findByLabelText(/contact/i)
      .type('New application contact');
    cy.get('form')
      .contains('Save')
      .click();

    // show
    cy.get('table')
      .contains('td', 'New application title')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'New application description')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'New application contact')
      .should('be.visible');

    // update
    cy.findByText('edit').click();

    cy.get('form')
      .findByLabelText(/name/i)
      .clear()
      .type('Changed application title');
    cy.get('form')
      .findByLabelText(/description/i)
      .clear()
      .type('Changed application description');
    cy.get('form')
      .findByLabelText(/contact/i)
      .clear()
      .type('Changed application contact');
    cy.get('form')
      .contains('Save')
      .click();

    // show
    cy.get('table')
      .contains('td', 'Changed application title')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'Changed application description')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'Changed application contact')
      .should('be.visible');

    // delete
    cy.findByText('delete').click();
    cy.findByText('Delete').click();

    cy.findByText('There are no applications available.');
  });
});
