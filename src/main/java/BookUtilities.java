import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookUtilities {

    /**
     * Code Smell   : Feature Envy
     * Fix          : Accepted as part of the Utility/Service pattern. The logic is kept here
     *                to separate business operations from the Book data object.
     *                (Replaced java.util.Date with java.time.LocalDate to eliminate magic number MILLIS_IN_A_DAY).
     */
    public long calculateOverdueDays(Book book) {
        if (book.getDueDate() == null) {
            return 0;
        }
        
        LocalDate today = LocalDate.now();
        if (book.getDueDate().isBefore(today)) {
            return ChronoUnit.DAYS.between(book.getDueDate(), today);
        }
        return 0;
    }

    /**
     * Code Smell   : Duplicate Code
     * Fix          : Reused the calculateOverdueDays method instead of copy-pasting the date logic.
     */
    public boolean isBookOverdue(Book book) {
        return calculateOverdueDays(book) > 0;
    }

    /**
     * Code Smell   : Conditional Complexity (If-Else Chains)
     * Fix          : Removed the if-else block. Delegated the fine rate retrieval 
     *                directly to the BookCategory Enum (Polymorphism).
     */
    public double calculateFine(Book book) {
        long overdueDays = calculateOverdueDays(book);
        if (overdueDays <= 0) {
            return 0.0;
        }
        
        return overdueDays * book.getCategory().getFineRate();
    }
    
    public String generateBookReport(Book book) {
        StringBuilder report = new StringBuilder();
        
        report.append("=== BOOK REPORT ===\n");
        report.append("ISBN: ").append(book.getIsbn()).append("\n");
        report.append("Title: ").append(book.getTitle()).append("\n");
        report.append("Author: ").append(book.getAuthor()).append("\n");
        report.append("Category: ").append(book.getCategory().name()).append("\n");
        report.append("Available: ").append(book.isAvailable() ? "Yes" : "No").append("\n");
        
        if (!book.isAvailable()) {
            report.append("Borrower: ").append(book.getBorrowerMemberId()).append("\n");
            report.append("Due Date: ").append(book.getDueDate()).append("\n");
            
            if (isBookOverdue(book)) {
                report.append("Overdue Days: ").append(calculateOverdueDays(book)).append("\n");
                report.append("Fine Amount: $").append(calculateFine(book)).append("\n");
            }
        }
        
        report.append("==================\n");
        return report.toString();
    }
    
    /**
     * Code Smell   : Switch Statements
     * Fix          : Removed the switch block completely. Replaced with a direct boolean
     *                check from the BookCategory Enum.
     */
    public boolean isPopularCategory(BookCategory category) {
        return category.isPopular();
    }
}