# Code Smells Documentation

This document lists the six deliberately implemented code smells in the Library Management System, along with their locations and justifications.

## 1. Long Method
**Location:** `LibrarySystem.java`, lines 32-122 (borrowBook method)
**Justification:** The `borrowBook` method is excessively long at 90+ lines and handles multiple responsibilities including member validation, member creation, book validation, book creation, availability checking, overdue calculation, borrowing logic, and detailed reporting. This method violates the Single Responsibility Principle and is difficult to maintain and test.

## 2. God Class (Blob)
**Location:** `LibrarySystem.java`, entire class (lines 1-204)
**Justification:** The `LibrarySystem` class is a God Class that handles too many responsibilities including book management, member management, borrowing/returning logic, search functionality, validation, fine calculation, transaction logging, and system statistics. It should be broken down into separate classes like BookManager, MemberManager, TransactionManager, etc.

## 3. Duplicated Code
**Location:** 
- `MemberValidator.java`, lines 13-19 and lines 46-52 (member validation logic)
- `BookUtilities.java`, lines 8-16 and lines 20-28 (date calculation logic)

**Justification:** The same validation logic for member information (name, email, phone checks) is repeated in two different methods in `MemberValidator`. Similarly, the date calculation logic for determining overdue days is duplicated in `BookUtilities`. This code should be extracted into reusable private methods.

## 4. Large Parameter List
**Location:** `MemberValidator.java`, lines 10-12 (validateMemberForBorrowing method)
**Justification:** The `validateMemberForBorrowing` method takes 11 parameters, making it difficult to call and understand. Many of these parameters are related and could be grouped into objects or the method could be split into smaller, more focused methods.

## 5. Magic Numbers
**Location:** Multiple locations:
- `LibrarySystem.java`, lines 45, 50, 55, 91, 127 (values like 2, 10, 5, 25.0, 6, 50)
- `MemberValidator.java`, lines 23, 27, 35, 43, 49, 58-74 (values like 5, 25.0, 6, 50, 5, 0.1, 0.05, etc.)
- `BookUtilities.java`, lines 17, 30, 61-69 (values like 86400000, 0.5, 1.0, 0.25)

**Justification:** Hardcoded numeric values appear throughout the codebase without explanation. These should be extracted into named constants with meaningful names to improve readability and maintainability.

## 6. Feature Envy
**Location:** 
- `MemberValidator.java`, lines 58-74 (calculateMemberRisk method)
- `BookUtilities.java`, lines 34-70 (generateBookReport method)

**Justification:** Both methods are excessively interested in the internal data of other classes. The `calculateMemberRisk` method accesses multiple Member properties extensively, and `generateBookReport` accesses many Book properties. These methods would be better placed as methods within the Member and Book classes respectively, or the logic should be refactored to reduce the dependency on external object data.

## Summary
All six code smells have been deliberately implemented to demonstrate poor coding practices while maintaining functional correctness. The system works as intended but suffers from maintainability, readability, and extensibility issues due to these anti-patterns.