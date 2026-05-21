import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * CODE SMELL: God Class (Blob) - Class holds too many responsibilities
 * FIX: Delegate behaviors to specialized helper classes (MemberValidator, BookUtilities)
 */

public class LibrarySystem {
    private final Map<String, Book> books;
    private final Map<String, Member> members;
    private final List<String> transactionHistory;
    private final MemberValidator memberValidator;
    private final BookUtilities bookUtilities;
    
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
    
    public LibrarySystem() {
        this.books = new HashMap<>();
        this.members = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
        this.memberValidator = new MemberValidator();
        this.bookUtilities = new BookUtilities();
    }
    
   /** 
    * CODE SMELL (1) : Long Parameter List (LPL) & Data Clumps
    * FIX            : Group parameters into a generic Map (or ideally, a dedicated Parameter Object / DTO class)
    */ 
    
    /**
     * CODE SMELL (2) : Long Method - Function performs multiple distinct steps in one block
     * FIX            : Use Extract Method refactoring to split into smaller, cohesive private functions
     */
    
    public boolean borrowBook(Map<String, Object> details) {
        if (details == null) return false;
        
        String memberId = (String) details.get("memberId");
        String isbn = (String) details.get("isbn");
        int borrowDays = (int) details.getOrDefault("borrowDays", 0);
        
        Member member = findOrCreateMember(details);
        if (member == null) return false; 
        
        /**
         * CODE SMELL   : Magic Numbers (Specify at Member.java)
         * Fix          : Replaced magic numbers with named constants to improve readability and maintainability.
         */
        if (!memberValidator.validateMemberForBorrowing(member, false)) {
            System.out.println("Member " + memberId + " is not eligible to borrow books.");
            return false;
        }
        
        Book book = findOrCreateBook(details);
        if (book == null) return false;
        
        if (!book.isAvailable()) {
            handleUnavailableBook(book);
            return false;
        }
        
        LocalDate dueDate = calculateDueDate(borrowDays);
        executeBorrowing(member, book, dueDate, borrowDays);
        
        return true;
    }
    
    public boolean returnBook(String isbn) {
        Book book = books.get(isbn);
        if (book == null) {
            System.out.println("Book not found: " + isbn);
            return false;
        }
        
        if (book.isAvailable()) {
            System.out.println("Book is not currently borrowed: " + book.getTitle());
            return false;
        }
        
        Member member = members.get(book.getBorrowerMemberId());
        LocalDate today = LocalDate.now();
        
        double fine = bookUtilities.calculateFine(book);
        if (fine > 0 && member != null) {
            member.setTotalFines(member.getTotalFines() + fine);
        }
        
        book.setAvailable(true);
        book.setDueDate(null);
        book.setBorrowerMemberId(null);
        if (member != null) {
            member.removeBorrowedBook(isbn);
        }
        
        String transaction = "RETURN: Book " + isbn + " returned on " + today.format(DATE_FORMATTER) + 
                           (fine > 0 ? " with fine $" + fine : "");
        transactionHistory.add(transaction);
        
        System.out.println("Book returned successfully" + (fine > 0 ? " with fine: $" + fine : ""));
        return true;
    }

    private Member findOrCreateMember(Map<String, Object> details) {
        String id = (String) details.get("memberId");
        boolean isNew = (boolean) details.getOrDefault("isNewMember", false);
        
        Member member = members.get(id);
        if (member == null && isNew) {
            String name = (String) details.get("memberName");
            String email = (String) details.get("memberEmail");
            String phone = (String) details.get("memberPhone");
            String address = (String) details.get("memberAddress");
            
            if (!memberValidator.validateMemberForRegistration(name, email, phone, address)) {
                System.out.println("Invalid member registration details provided.");
                return null;
            }
            member = new Member(id, name, email, phone, address);
            members.put(id, member);
            System.out.println("New member created: " + name);
        } else if (member == null) {
            System.out.println("Member not found: " + id);
        }
        return member;
    }

    private Book findOrCreateBook(Map<String, Object> details) {
        String isbn = (String) details.get("isbn");
        Book book = books.get(isbn);
        if (book == null) {
            String title = (String) details.get("bookTitle");
            String author = (String) details.get("bookAuthor");
            String category = (String) details.get("bookCategory");
            
            if (title != null && author != null && category != null) {
                BookCategory bookCategory = BookCategory.fromString(category);
                book = new Book(isbn, title, author, bookCategory);
                books.put(isbn, book);
                System.out.println("New book added to library: " + title);
            } else {
                System.out.println("Book not found and insufficient information to create new book.");
                return null;
            }
        }
        return book;
    }

    private void handleUnavailableBook(Book book) {
        System.out.println("Book is currently not available: " + book.getTitle());
        if (book.getDueDate() != null) {
            System.out.println("Estimated return date: " + book.getDueDate().format(DATE_FORMATTER));
            if (bookUtilities.isBookOverdue(book)) {
                System.out.println("Book is overdue by " + bookUtilities.calculateOverdueDays(book) + 
                                   " days. Fine: $" + bookUtilities.calculateFine(book));
            }
        }
    }

    private LocalDate calculateDueDate(int borrowDays) {
        return LocalDate.now().plusDays(borrowDays);
    }

    private void executeBorrowing(Member member, Book book, LocalDate dueDate, int borrowDays) {
        book.setAvailable(false);
        book.setDueDate(dueDate);
        book.setBorrowerMemberId(member.getMemberId());
        member.addBorrowedBook(book.getIsbn());
        
        String transaction = "BORROW: Member " + member.getMemberId() + " borrowed book " + book.getIsbn() + 
                           " (" + book.getTitle() + ") on " + LocalDate.now().format(DATE_FORMATTER) + 
                           " due " + dueDate.format(DATE_FORMATTER);
        transactionHistory.add(transaction);
        
        printBorrowingConfirmation(member, book, dueDate, borrowDays);
    }

    private void printBorrowingConfirmation(Member member, Book book, LocalDate dueDate, int borrowDays) {
        System.out.println("=== BORROWING CONFIRMATION ===");
        System.out.println("Member: " + member.getName() + " (" + member.getMemberId() + ")");
        System.out.println("Book: " + book.getTitle() + " by " + book.getAuthor());
        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("Category: " + book.getCategory());
        System.out.println("Borrow Date: " + LocalDate.now().format(DATE_FORMATTER));
        System.out.println("Due Date: " + dueDate.format(DATE_FORMATTER));
        System.out.println("Borrowing Period: " + borrowDays + " days");
        
        double dailyRate = bookUtilities.calculateFine(book);
        System.out.println("Daily Fine Rate: $" + dailyRate); 
        
        System.out.println("Books Currently Borrowed: " + member.getBorrowedBooks().size());
        System.out.println("Total Books Borrowed (Lifetime): " + member.getBorrowCount());
        System.out.println("==============================");
    }

    public void addBook(String isbn, String title, String author, String category) {
        if (books.containsKey(isbn)) return;
        BookCategory bookCategory = BookCategory.fromString(category);
        books.put(isbn, new Book(isbn, title, author, bookCategory));
    }
    
    public void addMember(String memberId, String name, String email, String phone, String address) {
        if (members.containsKey(memberId)) return;
        members.put(memberId, new Member(memberId, name, email, phone, address));
    }
    
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) return new ArrayList<>();
        List<Book> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase().trim();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(lowerQuery) ||
                book.getAuthor().toLowerCase().contains(lowerQuery) ||
                book.getIsbn().contains(lowerQuery)) {
                results.add(book);
            }
        }
        return results;
    }
    
    public Member getMember(String memberId) { return members.get(memberId); }
    public Book getBook(String isbn) { return books.get(isbn); }
    public int getTotalBooks() { return books.size(); }
    public int getTotalMembers() { return members.size(); }
}