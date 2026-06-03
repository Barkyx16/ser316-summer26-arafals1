# Black Box Testing Report - Assignment 2

**Student Name:** Alexander Rafalski 
**ASU ID:** 1224381808 (arafals1) 
**Date:** 06/02/2026

## Part 1: Equivalence Partitioning (EP)

Identify equivalence partitions for the `checkoutBook(Book book, Patron patron)` method based on the specification (JavaDoc).

### Your EP Tables (add as many as needed)

### EP Table 1: Book Validity

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 1.1 | Book is null | Invalid | book == null | 2.1 | Checkout stops immediately |
| EP 1.2 | Reference book | Invalid | book type is REFERENCE | 5.0 | Book cannot be checked out |
| EP 1.3 | Available book | Valid | availableCopies > 0 | Success | Checkout may continue |
| EP 1.4 | Unavailable book | Invalid | availableCopies <= 0 | 2.0 | No checkout occurs |

## Part 2: Boundary Value Analysis (BVA)

Important BVA cases may overlap with EP. That is OK. You can reference all relevant EP/BVA coverage in Part 3.

### EP Table 1: Book Conditions

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 1.1 | Book is null | Invalid | book == null | 2.1 | Checkout should not be completed because there are no books available  |
| EP 1.2 | Reference book | Invalid | Book type is REFERENCE | 5.0 | Reference can't be checked out therefore, the transaction can not be completed |
| EP 1.3 | Available book | Valid | availableCopies > 0 | Success | Checkout can be processed and will work perfectly fine |
| EP 1.4 | Unavailable book | Invalid | availableCopies <= 0 | 2.0 | Book can't be checked out because of no copies available |

### EP Table 2: Patron Conditions

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 2.1 | Patron is null | Invalid | patron == null | 3.1 | Checkout should not work because there is no person available |
| EP 2.2 | Suspended patron | Invalid | Account is suspended | 3.0 | Person is not allowed to check out books. |
| EP 2.3 | Too many overdue books | Invalid | overdueCount >= 3 | 4.0 | Checkout should not work and be denied |
| EP 2.4 | Unpaid fines | Invalid | fineBalance >= 10.00 | 4.1 | Checkout should not go through until the fines are paid |
| EP 2.5 | Eligible patron | Valid | Passes all eligibility checks | Continue | Checkout can be processed and onto the next step|

### EP Table 3: Renewal Conditions

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 3.1 | Renewal | Valid | Person already has the book checked out | 0.1 | Due date should be updated and available copies should not change at all |
| EP 3.2 | Not a renewal | Valid | Person does not already have the book | Continue | Checkout continues normally and works |

### EP Table 4: Checkout Limits

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 4.1 | Below limit | Valid | Checkout count is below the maximum | Continue | Person can check out the book with ease |
| EP 4.2 | At maximum limit | Invalid | Checkout count equals maximum allowed | 3.2 | Checkout should be declined and reworked |
| EP 4.3 | Near checkout limit | Valid | Person is within 2 books of the limit after checkout | 1.1 | Checkout works but gives a warning |

### EP Table 5: Successful Checkout Results

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 5.1 | Normal checkout | Valid | All checks pass and no warnings apply | 0.0 | Book is checked out with no issues |
| EP 5.2 | Warning for overdue books | Valid | Person has 1 or 2 overdue books | 1.0 | Checkout works but a warning is showed |

---

### Your BVA Tables (add more as needed)

### BVA Table 1: Overdue Books Boundary

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA 1.1 | Below | overdueCount = 2 | 1.0 | Checkout is allowed with a warning |
| BVA 1.2 | At | overdueCount = 3 | 4.0 | Checkout should not work and be denied |
| BVA 1.3 | Above | overdueCount = 4 | 4.0 | Checkout should still not work and be denied |

### BVA Table 2: Fine Boundary

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA 2.1 | Below | fineBalance = 9.99 | Success | Fine is below the limit |
| BVA 2.2 | At | fineBalance = 10.00 | 4.1 | Checkout should not work and be denied |
| BVA 2.3 | Above | fineBalance = 10.01 | 4.1 | Checkout should still not work and be denied |

### BVA Table 3: Student Checkout Limit Boundary

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA 3.1 | Below | checkoutCount = 9 | Success | Student is still below the limit |
| BVA 3.2 | At | checkoutCount = 10 | 3.2 | Student has reached the limit |
| BVA 3.3 | Above | checkoutCount = 11 | 3.2 | Student is over the limit |

### BVA Table 4: Warning Boundary

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA 4.1 | Below | After checkout count = 7 | 0.0 | It is not close enough to the current trigger limit |
| BVA 4.2 | At | After checkout count = 8 | 1.1 | Within two books of the limit |
| BVA 4.3 | Above | After checkout count = 9 | 1.1 | Warning should still be shown |

---

### Test Case Table
At least some of your tests should verify observable state changes, not just return values.
| Test ID Name                            | EP/BVA                 | Input Description                         | Expected Return | Expected State Changes                                                     | Checkout0 | Checkout1 | Checkout2 | Checkout3 |
| --------------------------------------- | ---------------------- | ----------------------------------------- | --------------- | -------------------------------------------------------------------------- | 
| T1 testNullPatron                       | EP 2.1                 | Valid book but no person                  | 3.1             | Nothing should change                                                      |           |           |           |           |
| T2 testNullBook                         | EP 1.1                 | No book and eligible person               | 2.1             | Nothing should change                                                      |           |           |           |           |
| T3 testSuspendedPatron                  | EP 2.2                 | Suspended person tries checkout           | 3.0             | Book should not be checked out                                             |           |           |           |           |
| T4 testThreeOverdueBooks                | EP 2.3, BVA 1.2        | Person has 3 overdue books                | 4.0             | Nothing should change                                                      |           |           |           |           |
| T5 testFourOverdueBooks                 | EP 2.3, BVA 1.3        | Person has 4 overdue books                | 4.0             | Nothing should change                                                      |           |           |           |           |
| T6 testFineAtLimit                      | EP 2.4, BVA 2.2        | Person owes exactly $10                   | 4.1             | Nothing should change                                                      |           |           |           |           |
| T7 testFineAboveLimit                   | EP 2.4, BVA 2.3        | Person owes more than $10                 | 4.1             | Nothing should change                                                      |           |           |           |           |
| T8 testFineBelowLimit                   | BVA 2.1                | Person owes $9.99                         | 0.0             | Book added to person's checked-out list and available copies decrease by 1 |           |           |           |           |
| T9 testUnavailableBook                  | EP 1.4                 | Book has no available copies              | 2.0             | Nothing should change                                                      |           |           |           |           |
| T10 testReferenceBook                   | EP 1.2                 | Reference book checkout attempt           | 5.0             | Nothing should change                                                      |           |           |           |           |
| T11 testNormalCheckout                  | EP 1.3, EP 2.5, EP 5.1 | Eligible patron checks out available book | 0.0             | Book added to person's checked-out list and available copies decrease by 1 |           |           |           |           |
| T12 testRenewal                         | EP 3.1                 | Person already has the book               | 0.1             | Due date updates but available copies stay the same                        |           |           |           |           |
| T13 testOneOverdueWarning               | EP 5.2                 | Person has 1 overdue book                 | 1.0             | Book added to person's checked-out list and available copies decrease by 1 |           |           |           |           |
| T14 testTwoOverdueWarning               | EP 5.2, BVA 1.1        | Person has 2 overdue books                | 1.0             | Book added to person's checked-out list and available copies decrease by 1 |           |           |           |           |
| T15 testStudentAtMaxLimit               | EP 4.2, BVA 3.2        | Student already has 10 books              | 3.2             | Nothing should change                                                      |           |           |           |           |
| T16 testStudentBelowMaxLimit            | EP 4.1, BVA 3.1        | Student has 9 books                       | 1.1             | Book added to person's checked-out list and available copies decrease by 1 |           |           |           |           |
| T17 testStudentNearLimitWarning         | EP 4.3, BVA 4.2        | Student has 7 books before checkout       | 1.1             | Book added to person's checked-out list and checkout count increases       |           |           |           |           |
| T18 testStudentNoLimitWarning           | BVA 4.1                | Student has 6 books before checkout       | 0.0             | Book added to person's checked-out list and available copies decrease by 1 |           |           |           |           |
| T19 testFacultyAtMaxLimit               | EP 4.2                 | Faculty already has 20 books              | 3.2             | Nothing should change                                                      |           |           |           |           |
| T20 testPublicAtMaxLimit                | EP 4.2                 | Public person already has 5 books         | 3.2             | Nothing should change                                                      |           |           |           |           |
| T21 testChildAtMaxLimit                 | EP 4.2                 | Child already has 3 books                 | 3.2             | Nothing should change                                                      |           |           |           |           |
| T22 testValidationOrderPatronBeforeBook | EP 2.1, EP 1.1         | Person and book are both null             | 3.1             | Person check should happen first                                           |                                       
 

---

## Part 4: Bug Analysis

### Easter Eggs Found

List any easter egg messages you observed:

* Easter Egg #10.1
* Easter Egg #10.1/3
* Easter Egg #10.2
* Easter Egg #10.2/3
* Easter Egg #10.3
* Easter Egg #10.3/3
* Easter Egg #13
* Easter Egg #14
* Easter Egg #15.1
* Easter Egg #15.2
* Easter Egg #17
* Easter Egg #18
* Easter Egg #19
* Easter Egg #20

### Implementation Results

| Implementation | Bugs Found (count) |
| -------------- | ------------------ |
| Checkout0      | 4                  |
| Checkout1      | 5                  |
| Checkout2      | 3                  |
| Checkout3      | 2                  |

### Bugs Discovered

Bugs Discovered
Checkout0:

Bug 1: The copies of the book do not actually go down after someone checks out a book. — Revealed by: T11 Normal Checkout
Bug 2: Reference books come back with 2.0 but they should come back with 5.0. — Revealed by: T10 Reference Book
Bug 3: When a person with a fine under the limit press check out,  the copy of the book doesn’t work like it should. — Revealed by: T8 Fine Below Limit
Bug 4: Issues happen when the checkout gets pressed with an overdue warning. — Revealed by: T13 One Overdue Warning

Checkout1:

Bug 1: Books never show up in the person’s checked out list when they complete their checkout.. — Revealed by: T11 Normal Checkout
Bug 2: When a person hits their max, it returns 1.1 instead of 3.2. — Revealed by: T15 Student At Max Limit
Bug 3: Faculty people at their limits have the same return code issue. — Revealed by: T19 Faculty At Max Limit
Bug 4: Public people at their limit also return 1.1 instead of 3.2. — Revealed by: T20 Public At Max Limit
Bug 5: Children hit the same wrong return button when they are maxed out. — Revealed by: T21 Child At Max Limit

Checkout2:

Bug 1: Unavailable books are returning 0.0 instead of 2.0. — Revealed by: CheckoutBlackBoxSample T1
Bug 2: Renewals come back as 0.0 instead of 0.1. — Revealed by: T12 Renewal
Bug 3: Student max checkout limit returns 1.1 instead of 3.2. — Revealed by: T15 Student At Max Limit

Checkout3:

Bug 1: A person with two overdue books gets 0.0 instead of 1.0. — Revealed by: T14 Two Overdue Warning
Bug 2: Renewals are messing with the available copy count when it should not be messed with. — Revealed by: T12 Renewal

### Comparative Analysis

The most critical bugs that I found were the ones that broke the core checkout functionality. Checkout1 was the most problematic because the books were not added to the person’s checked out list after a successful checkout. Next, Checkout0 also had some issues because the book availability was not updated correctly and the reference books ended up returned and threw an error code. 


If I had to choose one implementation, I would end up choosing Checkout3 because it had the fewest bugs thrown and the checkout process worked correctly way more than it failed. The only issues it had were overdue warnings and renewal handlings. Checkout1 and Checkout0 had a lot more frequent problems affecting the library use. Lastly, Checkout2 had several incorrect return codes that could mess up the users checkout. 

---

## Part 5: Reflection
Which testing technique was most effective for finding bugs?

The BVA testing technique was definitely most effective for finding bugs because most of the bugs only showed up at the very end at the boundary values like overdue counts, checkout limits, and fine amounts.Some of those bugs might not have been caught with only normal test cases.

What was the most challenging aspect of this assignment?

The most challenging aspect of this assignment was I had to make sure my tests were checking the actual state changes and just the code that got returned. This took me a while because I had to figure out the scenarios that actually mattered to this assignment.

How did you decide on your EP and BVA?

How I decided on my EP and BVA was that for EP I went through the spec and split inputs and put them into valid and invalid groups. On the other hand, for BVA I looked at where there was a hard limit and tested it right at and a few lines below and above. This is where most of the bugs came up in my testing. 


Describe one test where checking only the return value would NOT have been sufficient to detect a bug.

T11 Normal Checkout was the test where checking only the return value would NOT have been sufficient to detect a bug because it initially returned 0.0 which was fine, but the book was never added to the persons list or updated the available copies of the book. Lastly, a return value alone would have missed that completely and it would have been left undetected.


