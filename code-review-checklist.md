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
| 2 | Checkout.java | 12 | CG | MAX_FINE_AMOUNT is not the final word and it could be fixed. | Medium |
| 3 | Checkout.java | 14 | CS | bookList is a Map. It is not a list this name is incorrect and could confuse people. | Low |
| 4 | Checkout.java | 16 | MD | The comment for history is very incomplete and is not explained well at all and needs to be fixed. | Low |
| 5 | Checkout.java | 78-160 | CG | checkoutBook() handles almost all of the tasks this needs to be changed so it is easier to understand | Medium |
| 6 | | | | | |
| 7 | | | | | |
| 8 | | | | | |
| 9 | | | | | |
| 10 | | | | | |

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
