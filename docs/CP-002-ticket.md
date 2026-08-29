# Inform the invoice due day during onboarding

## Goal

Allow users to inform their recurring invoice due day while completing onboarding.
The selected day defines the active invoice boundary and is reused when the app
creates future monthly invoices.

## Business Impact

Cow Paw currently creates the first invoice with a due date seven days after
onboarding. This hidden default may not match the user's real credit card due
date and makes invoice tracking less reliable.

Collecting the recurring due day makes the first invoice accurate, gives future
invoices a predictable cycle, and prevents transaction recording from stopping
after the onboarding month.

## Acceptance Criteria

- The existing onboarding screen asks for the recurring invoice due day.
- The user can select any day from 1 through 31.
- The user must explicitly select a day before the **Comecar** action is enabled.
- The selected day is saved and reused for future invoices.
- The first invoice is due on the next occurrence of the selected day.
- Selecting today's day uses the next month because the invoice boundary starts
  at the beginning of the due date.
- When a selected day does not exist in a month, the invoice is due on that
  month's final day.
- Transactions received before the due date belong to the active invoice.
- At local midnight at the start of the due date, the app starts using an
  invoice for the next due-date occurrence.
- Opening the app or receiving a transaction creates the next invoice when no
  active invoice exists.
- Repeated or concurrent active-invoice requests do not create duplicate
  invoices.
- Onboarding is completed only after the due day and first invoice are saved.
- The selected day remains visible after an onboarding screen recreation.
- Existing installations may reset their local invoice data and complete the
  updated onboarding again.

## Product Requirements

- Design: preserve the existing onboarding visual language. No Figma or other
  design specification is available.
- UI content: use the existing Portuguese base string resources.
- Localization: no additional locales are required in this ticket.
- Analytics: no events or analytics integration are required in this ticket.
- API: Cow Paw has no remote API, so no API contract changes are required.

## Out Of Scope

- Editing the due day after onboarding.
- Configuring a separate invoice closing day.
- Displaying invoice history.
- Adding analytics or remote synchronization.
