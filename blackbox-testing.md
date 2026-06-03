# Black Box Testing Report - Assignment 2

**Student Name:** Alexander Rafalski 
**ASU ID:** 1224381808 (arafals1) 
**Date:** 06/02/2026

---

## Part 1: Equivalence Partitioning (EP)

Identify equivalence partitions for the `checkoutBook(Book book, Patron patron)` method based on the specification (JavaDoc).

Create **multiple tables**, one per partition category (e.g., book state, patron state, renewal, limits, etc.).

Do **not** put everything into one table.

**Column Explanations:**
- **Partition ID**: Unique identifier (e.g., EP 1.1, EP 2.1)
- **State**: The specific state/value for this partition (e.g., "Unavailable", "Available")
- **Valid/Invalid**: Whether this partition represents valid or invalid input
- **Input Condition**: Precise condition that defines this partition
- **Expected Return**: What return code you expect
- **Expected Behavior**: What should happen

### Example EP Table: Book Availability

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 1.1 | Unavailable (0 copies) | Invalid | availableCopies == 0 AND other conditions allow checkout | 2.0 | No copies to checkout |
| EP 1.2 | Available (1+ copies) | Valid | availableCopies > 0 AND other conditions allow checkout | Success | Book can be checked out |

**Example test cases:** `testBookAvailable()`, `testUnavailableBook()`

---

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
| EP 2.1 | Patron is null | Invalid | patron == null | 3.1 | Checkout should not work because there is no patron available |
| EP 2.2 | Suspended patron | Invalid | Account is suspended | 3.0 | Patron is not allowed to check out books. |
| EP 2.3 | Too many overdue books | Invalid | overdueCount >= 3 | 4.0 | Checkout should not work and be denied |
| EP 2.4 | Unpaid fines | Invalid | fineBalance >= 10.00 | 4.1 | Checkout should not go through until the fines are paid |
| EP 2.5 | Eligible patron | Valid | Passes all eligibility checks | Continue | Checkout can be processed and onto the next step|

### EP Table 3: Renewal Conditions

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 3.1 | Renewal | Valid | Patron already has the book checked out | 0.1 | Due date should be updated and available copies should not change at all |
| EP 3.2 | Not a renewal | Valid | Patron does not already have the book | Continue | Checkout continues normally and works |

### EP Table 4: Checkout Limits

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 4.1 | Below limit | Valid | Checkout count is below the maximum | Continue | Patron can check out the book with ease |
| EP 4.2 | At maximum limit | Invalid | Checkout count equals maximum allowed | 3.2 | Checkout should be declined and reworked |
| EP 4.3 | Near checkout limit | Valid | Patron is within 2 books of the limit after checkout | 1.1 | Checkout works but gives a warning |

### EP Table 5: Successful Checkout Results

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 5.1 | Normal checkout | Valid | All checks pass and no warnings apply | 0.0 | Book is checked out with no issues |
| EP 5.2 | Warning for overdue books | Valid | Patron has 1 or 2 overdue books | 1.0 | Checkout works but a warning is showed |

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

## Part 3: Test Cases Designed

List at least **20** test cases you designed based on your EP/BVA analysis.

Each test case should include:
- EP/BVA coverage
- specific inputs / setup
- expected return code
- expected **observable state changes** (if any)

> Do not test console output.

### Test Case Table
At least some of your tests should verify observable state changes, not just return values.

**Checkout0-3 Columns:** Mark each implementation as Pass (✓) or Fail (✗) for this test case. This helps you track which implementations have bugs and will be useful for Part 4 analysis.

| Test ID Name | EP/BVA | Input Description | Expected Return | Expected State Changes | Checkout0 | Checkout1 | Checkout2 | Checkout3 |
|--------------|--------|-------------------|-----------------|------------------------|-----------|-----------|-----------|-----------|
| T1 testUnavailableBook | EP 1.1 | Book unavailable (0 copies), eligible patron | 2.0 | No state change | ✓ | ✓ | ✗ | ✓ |
| T2 testBookAvailable | EP 1.2 | Book available (1+ copies), eligible patron, no warnings normal checkout | 0.0 | Patron map updated; copies of book change | ✗ | ✗ | ✓ | ✓ |

(Add rows until you have at least 20.)

---

## Part 4: Bug Analysis

### Easter Eggs Found
List any easter egg messages you observed:
- 
- 

### Implementation Results

| Implementation | Bugs Found (count) |
|----------------|---------------------|
| Checkout0      | |
| Checkout1      | |
| Checkout2      | |
| Checkout3      | |

### Bugs Discovered
List distinct bugs you identified for each implementation. Each bug must cite at least one test case that revealed it.

**Checkout0:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

**Checkout1:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

**Checkout2:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

**Checkout3:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

### Comparative Analysis
Compare the four implementations:
- Which bugs are most critical (cause the worst failures)?
- Which implementation would you use if you had to choose?
- Why? Justify your choice considering bug severity and frequency.

---

## Part 5: Reflection

**Which testing technique was most effective for finding bugs?**

**What was the most challenging aspect of this assignment?**

**How did you decide on your EP and BVA?**

**Describe one test where checking only the return value would NOT have been sufficient to detect a bug.**

