describe('Applications', () => {
  it('should create and show a new application', () => {
    cy.visit('/');

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

    cy.get('table')
      .contains('td', 'New application title')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'New application description')
      .should('be.visible');
    cy.get('table')
      .contains('td', 'New application contact')
      .should('be.visible');
  });
});
