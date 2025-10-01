# Library Management System - Deliberately Smelly Code

This project demonstrates a functional Library Management System written in Java with intentionally implemented code smells for educational purposes.

## Project Structure
```
Assignment 2/
├── src/
│   ├── main/java/
│   │   ├── Book.java                 # Book entity class
│   │   ├── Member.java               # Member entity class  
│   │   ├── LibrarySystem.java        # Main system class (God Class)
│   │   ├── MemberValidator.java      # Utility class with Feature Envy
│   │   ├── BookUtilities.java       # Utility class with duplicated code
│   │   └── Main.java                 # Main class with demo and tests
│   └── test/java/
│       └── LibrarySystemTest.java    # Comprehensive unit tests
└── docs/
    └── smells.md                     # Documentation of code smells
```

## Features
- Add and manage books in the library
- Add and manage library members
- Borrow and return books with due date tracking
- Search books by title, author, or ISBN
- Member validation and risk assessment
- Fine calculation for overdue books
- Transaction history tracking
- Comprehensive unit testing

## Code Smells Implemented
1. **Long Method** - borrowBook() method in LibrarySystem.java
2. **God Class (Blob)** - LibrarySystem class handles too many responsibilities
3. **Duplicated Code** - Validation logic repeated in multiple places
4. **Large Parameter List** - validateMemberForBorrowing() method
5. **Magic Numbers** - Hardcoded values throughout the codebase
6. **Feature Envy** - Utility classes overly interested in other classes' data

## How to Run

### Compilation
```bash
cd "src/main/java"
javac *.java
```

### Execution
```bash
java Main
```

The program will:
1. Demonstrate library functionality
2. Show borrowing and returning processes
3. Display search results
4. Run 8 comprehensive unit tests
5. Show 100% test success rate

## Code Statistics
- **Total Lines of Code**: ~679 lines (including Main.java with tests)
- **Core System Code**: ~526 lines (excluding Main.java)
- **Test Coverage**: 8 unit tests covering all major functionality
- **Success Rate**: 100% (all tests pass despite code smells)

## Learning Objectives
This project demonstrates that:
- Code can be functional while still having poor design
- Code smells make maintenance and extension difficult
- Proper refactoring would improve code quality significantly
- Unit tests can validate functionality even with smelly code

See `docs/smells.md` for detailed documentation of each code smell with file locations and justifications.