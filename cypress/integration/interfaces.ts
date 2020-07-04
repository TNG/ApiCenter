describe("Interfaces", () => {
  it("should create, show, update and delete an interface", () => {
    cy.visit("/interfaces");

    // create
    cy.contains("add").click({ force: true });

    cy.get("form")
      .findByLabelText(/application/i)
      .click({ force: true });
    cy.get("mat-option")
      .contains("Application C")
      .click({ force: true });

    cy.get("form")
      .findByLabelText(/name/i)
      .type("New interface");
    cy.get("form")
      .findByLabelText(/description/i)
      .type("New interface description");

    cy.get("form")
      .findByLabelText(/type/i)
      .click({ force: true });
    cy.get("mat-option")
      .contains("GRAPHQL")
      .click({ force: true });

    cy.get("form")
      .contains("Save")
      .click({ force: true });

    // check if shown in table
    cy.get("table")
      .contains("td", "New interface")
      .should("be.visible");
    cy.get("table")
      .contains("td", "GRAPHQL")
      .should("be.visible");

    // check if shown on details page
    cy.get("table")
      .contains("td", "New interface")
      .click({ force: true });

    cy.get("table")
      .contains("td", "New interface")
      .should("be.visible");
    cy.get("table")
      .contains("td", "New interface description")
      .should("be.visible");
    cy.get("table")
      .contains("td", "GRAPHQL")
      .should("be.visible");

    // check if application data is shown on details page
    cy.get("table")
      .contains("td", "Application C")
      .should("be.visible");
    cy.get("table")
      .contains("td", "This is the third application")
      .should("be.visible");
    cy.get("table")
      .contains("td", "John Doe, john.doe@company.com")
      .should("be.visible");

    // check if interface is shown in application details page
    cy.get("mat-toolbar")
      .contains("button", "Applications")
      .click({ force: true });
    cy.get("table")
      .contains("td", "Application C")
      .click({ force: true });

    cy.get("table")
      .contains("td", "New interface")
      .should("be.visible");
    cy.get("table")
      .contains("td", "GRAPHQL")
      .should("be.visible");
    cy.get("table")
      .contains("td", "New interface")
      .click({ force: true });

    // update interface
    cy.findByText("Edit").click({ force: true });

    cy.get("form")
      .findByLabelText(/application/i)
      .click({ force: true });
    cy.get("mat-option")
      .contains("Application B")
      .click({ force: true });

    cy.get("form")
      .findByLabelText(/name/i)
      .clear()
      .type("Updated interface");
    cy.get("form")
      .findByLabelText(/description/i)
      .clear()
      .type("Updated interface description");

    cy.get("form")
      .findByLabelText(/type/i)
      .click({ force: true });
    cy.get("mat-option")
      .contains("REST")
      .click({ force: true });

    cy.get("form")
      .contains("Save")
      .click({ force: true });

    cy.get("table")
      .contains("td", "Updated interface")
      .should("be.visible");
    cy.get("table")
      .contains("td", "REST")
      .should("be.visible");

    cy.get("mat-toolbar")
      .contains("button", "Applications")
      .click({ force: true });
    cy.get("table")
      .contains("td", "Application B")
      .click({ force: true });

    cy.get("table")
      .contains("td", "Updated interface")
      .click({ force: true });

    // delete interface
    cy.findByText("Delete").click({ force: true });
    cy.get("mat-dialog-container")
      .findByText("Delete")
      .click({ force: true });

    cy.findByText("Updated interface").should("not.exist");
  });
});
