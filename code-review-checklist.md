# Code Review Checklist

**Reviewer Name:** Alexander Rafalski
**Date:** May 31st 2026
**Branch:** Review

## Instructions
Review ALL source files (in main not test) in the project and identify defects using the categories below. Log at least 5 defects total:
- At least 1 from CS (Coding Standards)
- At least 1 from CG (Code Quality/General)
- At least 1 from FD (Functional Defects)
- Remaining can be from any category

## Review Categories

- **CS**: Coding Standards (naming conventions, formatting, style violations)
- **CG**: Code Quality/General (design issues, code smells, maintainability)
- **FD**: Functional Defects (logic errors, incorrect behavior, bugs)
- **MD**: Miscellaneous (documentation, comments, other issues)

## Defect Log

| Defect ID | File | Line(s) | Category | Description | Severity |
|-----------|------|---------|----------|-------------|----------|
| 1 | Checkout.java | 254 | FD | It uses == to compare Strings instead of including and implementing .equals(). This may end up giving bad and incorrect answers as result | High |
| 2 | Checkout.java | 13 | CG | MAX_FINE_AMOUNT is used as a constant but is not final. Fixing it would help with accidental changes in the code. | Medium |
| 3 | Checkout.java | 14 | CS | bookList is a Map. It is not a list this name is incorrect and could confuse people. | Low |
| 4 | Checkout.java | 16 | MD | The comment for history is very incomplete and is not explained well at all and needs to be fixed. | Low |
| 5 | Checkout.java | 78-160 | CG | checkoutBook() handles almost all of the tasks but this to be changed for it to be able to do everything | Medium |
| 6 | Checkout.java | 142-145 | FD | The checkoutBook() method has not been implemented yet. Ita lways returns 0.0, this means a successful checkout will be done when this happens. | Critical |
| 7 | Checkout.java | 246 | FD | The isPatronType() method uses == to compare strings instead of .equals(). This can cause the method to return the same thing when they are that close together. | High |
| 8 | Book.java | 106 | FD | The returnBook() method checks if availableCopies is less than 100 before increasing it. It should compare against totalCopies instead so the number of available is the same as the number of books. | High |
| 9 | Book.java | 143 | CS | The toString() statement is very long and difficult to read. Splitting it up makes it easier to read.| Low |
| 10 | Book.java | 53, 86 | MD | The comments labeled Getters and Setters do not provide much info. The comments need to be better in order to do this task. | Low |

**Severity Levels:**
- **Critical**: Causes system failure, data corruption, or security issues
- **High**: Major functional defect or significant quality issue
- **Medium**: Moderate issue affecting maintainability or minor functional problem
- **Low**: Minor style issue or cosmetic problem

## Example Entry

| Defect ID | File          | Line(s) | Category | Description                                | Severity |
|-----------|---------------|---------|----------|--------------------------------------------|----------|
| 1 | Checkout.java | 17      | CS       | Variable bookList misleading - Map not List | Medium |
| 2 | Book.java     | 107     | FD       | Magic number 100 should be totalCopies      | High |

## Notes
- Be specific with line numbers
- Provide clear, actionable descriptions
- Consider: readability, maintainability, correctness, performance, security
- Focus on issues that impact code quality or functionality
