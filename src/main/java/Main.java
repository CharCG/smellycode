import java.util.*;

public class Main {
    private static int totalTests = 0;
    private static int passedTests = 0;
    
    private static Map<String, Object> createBorrowMap(
            String memberId, String isbn, int borrowDays, String memberName, 
            String memberEmail, String memberPhone, String memberAddress, 
            boolean isNewMember, String bookTitle, String bookAuthor, String bookCategory) {
        
        Map<String, Object> details = new HashMap<>();
        details.put("memberId", memberId);
        details.put("isbn", isbn);
        details.put("borrowDays", borrowDays);
        details.put("memberName", memberName);
        details.put("memberEmail", memberEmail);
        details.put("memberPhone", memberPhone);
        details.put("memberAddress", memberAddress);
        details.put("isNewMember", isNewMember);
        details.put("bookTitle", bookTitle);
        details.put("bookAuthor", bookAuthor);
        details.put("bookCategory", bookCategory);
        return details;
    }

    public static void main(String[] args) {
        System.out.println("=== Library Management System ===");
        System.out.println("Demonstrating clean functionality after refactoring\n");
        
        LibrarySystem library = new LibrarySystem();
        
        library.addBook("978-0134685991", "Effective Java", "Joshua Bloch", "Programming");
        library.addBook("978-0596009205", "Head First Design Patterns", "Eric Freeman", "Programming");
        library.addBook("978-0321356680", "Effective C++", "Scott Meyers", "Programming");
        
        library.addMember("M001", "Ahmed Ali", "ahmed.ali@email.com", "1234567890", "123 Gulberg Lane");
        library.addMember("M002", "Fatima Khan", "fatima.khan@email.com", "0987654321", "456 DHA Avenue");
        
        System.out.println("\n=== Borrowing Books ===");

        library.borrowBook(createBorrowMap("M001", "978-0134685991", 14, null, null, null, null, false, null, null, null));
        
        // Memulai seluruh skenario pengujian otomatis
        runAutomatedTests(library);
    }

    private static void runAutomatedTests(LibrarySystem library) {
        testSuccessfulBorrowing(library);
        testBorrowingNonExistentMember(library);
        testNewMemberRegistrationAndBorrowing(library);
        testNewBookAndMemberBorrowing(library);
        testBookSearch(library);
        testMemberValidation();
        testBookUtilitiesReport(library);
        testSystemStatistics(library);

        System.out.println("\n=== Test Execution Summary ===");
        System.out.println("Total Tests Run: " + totalTests);
        System.out.println("Total Tests Passed: " + passedTests);
        System.out.println("Total Tests Failed: " + (totalTests - passedTests));
    }

    // TEST 1: Meminjam buku yang terdaftar
    private static void testSuccessfulBorrowing(LibrarySystem library) {
        totalTests++;
        boolean result = library.borrowBook(createBorrowMap("M002", "978-0596009205", 7, null, null, null, null, false, null, null, null));
        if (result && !library.getBook("978-0596009205").isAvailable()) {
            System.out.println("✓ Test 1 PASSED: Successful borrowing");
            passedTests++;
        } else {
            System.out.println("✗ Test 1 FAILED: Successful borrowing");
        }
    }

    // TEST 2: Rejeksi jika ID Member tidak ada di sistem
    private static void testBorrowingNonExistentMember(LibrarySystem library) {
        totalTests++;
        boolean result = library.borrowBook(createBorrowMap("M999", "978-0321356680", 10, null, null, null, null, false, null, null, null));
        if (!result) {
            System.out.println("✓ Test 2 PASSED: Non-existent member rejection");
            passedTests++;
        } else {
            System.out.println("✗ Test 2 FAILED: Non-existent member rejection");
        }
    }

    // TEST 3: Registrasi member baru langsung saat meminjam
    private static void testNewMemberRegistrationAndBorrowing(LibrarySystem library) {
        totalTests++;
        boolean result = library.borrowBook(createBorrowMap("M003", "978-0321356680", 10, "John Doe", "john@email.com", "1122334455", "789 Oak Road", true, null, null, null));
        if (result && library.getMember("M003") != null) {
            System.out.println("✓ Test 3 PASSED: New member registration and borrowing");
            passedTests++;
        } else {
            System.out.println("✗ Test 3 FAILED: New member registration and borrowing");
        }
    }

    // TEST 4: Registrasi member baru DAN buku baru sekaligus saat meminjam
    private static void testNewBookAndMemberBorrowing(LibrarySystem library) {
        totalTests++;
        boolean result = library.borrowBook(createBorrowMap("M004", "TEST001", 5, "Muhammad Usman", "usman@email.com", "1234567890", "Model Town Lahore", true, "Clean Code", "Robert Martin", "Programming"));
        if (result && library.getBook("TEST001") != null) {
            System.out.println("✓ Test 4 PASSED: New book and member registration");
            passedTests++;
        } else {
            System.out.println("✗ Test 4 FAILED: New book and member registration");
        }
    }

    // TEST 5: Pengujian fitur cari buku
    private static void testBookSearch(LibrarySystem library) {
        totalTests++;
        List<Book> results = library.searchBooks("effective");
        if (results.size() >= 2) {
            System.out.println("✓ Test 5 PASSED: Book search");
            passedTests++;
        } else {
            System.out.println("✗ Test 5 FAILED: Book search");
        }
    }

    // TEST 6: Pengujian internal MemberValidator
    private static void testMemberValidation() {
        totalTests++;
        MemberValidator validator = new MemberValidator();
        boolean validationResult = validator.validateMemberForRegistration("Muhammad Usman", "usman.valid@email.com", "1234567890", "Model Town Lahore");
        if (validationResult) {
            System.out.println("✓ Test 6 PASSED: Member validation");
            passedTests++;
        } else {
            System.out.println("✗ Test 6 FAILED: Member validation");
        }
    }

    // TEST 7: Pengujian pelaporan via BookUtilities
    private static void testBookUtilitiesReport(LibrarySystem library) {
        totalTests++;
        BookUtilities utilities = new BookUtilities();
        Book testBook = library.getBook("TEST001");
        String report = utilities.generateBookReport(testBook);
        if (report.contains("BOOK REPORT")) {
            System.out.println("✓ Test 7 PASSED: Book utilities");
            passedTests++;
        } else {
            System.out.println("✗ Test 7 FAILED: Book utilities");
        }
    }

    // TEST 8: Pengujian statistik jumlah total buku dan member
    private static void testSystemStatistics(LibrarySystem library) {
        totalTests++;
        if (library.getTotalBooks() >= 4 && library.getTotalMembers() >= 3) {
            System.out.println("✓ Test 8 PASSED: System statistics");
            passedTests++;
        } else {
            System.out.println("✗ Test 8 FAILED: System statistics");
        }
    }
}