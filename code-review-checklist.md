# Code Review Checklist

**Reviewer Name:** Alexander Rafalski
**asurite:** arafals1
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

| Defect ID | File          | Line(s) | Category | Description                                                                                                            | Severity |
| --------- | ------------- | ------- | -------- | ---------------------------------------------------------------------------------------------------------------------- | -------- |
| 1         | Checkout.java | 254     | FD       | String values are compared using == instead of .equals(). This can cause incorrect results when checking patron types  | High     |
| 2         | Checkout.java | 12      | CG       | MAX_FINE_AMOUNT should be declared final since it is supposed to be a constant value                                   | Medium   |
| 3         | Checkout.java | 14      | CS       | The variable bookList is named like a List but is a HashMap, which is confusing and should be renamed                  | Low      |
| 4         | Checkout.java | 16      | MD       | The comment describing transaction history is vague and does not explain the purpose of the variable                   | Low      |
| 5         | Checkout.java | 78-160  | CG       | checkoutBook() handles too many responsibilities in one method, making it difficult to read                            | Medium   |
| 6         | Checkout.java | 170-185 | CS       | Variable name checkoutBookResult is long and does not follow the naming style                                          | Low      |
| 7         | Patron.java   | 103-109 | CG       | The getLoanPeriodDays() method uses a long chain of if/else statements that could be adjusted                          | Medium   |
| 8         | Patron.java   | 172-176 | FD       | hasBookCheckedOut() uses a bad if/else structure. Returning containsKey() directly would just cause errors             | Low      |
| 9         | Book.java     | 220-225 | MD       | Several methods lack JavaDoc comments, this makes it harder to understand                                              | Low      |

**Severity Levels:**
- **Critical**: Causes system failure, data corruption, or security issues
- **High**: Major functional defect or significant quality issue
- **Medium**: Moderate issue affecting maintainability or minor functional problem
- **Low**: Minor style issue or cosmetic problem

## Example Entry

| Defect ID | File          | Line(s) | Category | Description                                                                                                                               | Severity |
| --------- | ------------- | ------- | -------- | ----------------------------------------------------------------------------------------------------------------------------------------- | -------- |
| 1         | Checkout.java | 17      | CS       | Variable name `bookList` is misleading because the variable is a Map, not a List.                                                         | Medium   |
| 2         | Book.java     | 107     | FD       | A hard-coded value of 100 is used instead of the `totalCopies` variable, which may lead to incorrect behavior if inventory values change. | High     |



