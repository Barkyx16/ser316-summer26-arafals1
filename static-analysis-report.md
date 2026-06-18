# Assignment 4 - Static Analysis Report

Name: Alexander Rafalski

Asurite: arafals1

Date: 6/17/2026

## Task 1: Branch Comparison:

| Branch | Checkstyle Warnings | SpotBugs Issues |
|----------|------------------:|---------------:|
| Blackbox | 98 | 8 |
| Review | 97 | 6 |
| StaticAnalysis | 80 | 0 (Checkout.java) |

## Task 2: Checkstyle Improvements:

In the check style warnings I reduced the number of warnings in the checkout java file by replacing the numeric values that were repeated with different numbers and by cleaning up the overall format of the file.
My main focus was checkout java file because it was the majority of the assignment points.

I got the number of warnings from 98 in Blackbox to 80 in StaticAnalysis.

## Task 3: SpotBugs Improvements:

The Spotbugs reported a lot if issues in the file Checkout.Java and the transaction class. I fixed these errors by making copies, returning maps and by ensuring the fields in the Transaction class file were used.

After I made these changes, the report said 0 issues in both of the files.

