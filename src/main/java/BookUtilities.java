import java.util.Date;

public class BookUtilities {

    /**
     * Code Smell   : Magic Numbers
     * Fix          : Replaced magic numbers with clearly named constants at the top.
     */
    private static final double DEFAULT_FINE_RATE = 0.5;
    private static final double REFERENCE_FINE_RATE = 1.0;
    private static final double CHILDREN_FINE_RATE = 0.25;
    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

    public long calculateOverdueDays(Book book) {
        if (book.getDueDate() == null) {
            return 0;
        }
        
        Date today = new Date();
        if (book.getDueDate().before(today)) {
            return (today.getTime() - book.getDueDate().getTime()) / MILLIS_IN_A_DAY;
        }
        return 0;
    }

    /**
     * Code Smell   : Duplicate Code
     * Fix          : Reused the calculateOverdueDays method instead of copy-pasting the logic.
     */
    public boolean isBookOverdue(Book book) {
        return calculateOverdueDays(book) > 0;
    }

    /**
     * Code Smell   : Feature Envy
     * Fix          : Moved the fine calculation logic out of the report generator.
     */
    public double calculateFine(Book book) {
        long overdueDays = calculateOverdueDays(book);
        if (overdueDays <= 0) {
            return 0.0;
        }
        
        if (book.getCategory().equals("Reference")) {
            return overdueDays * REFERENCE_FINE_RATE;
        } else if (book.getCategory().equals("Children")) {
            return overdueDays * CHILDREN_FINE_RATE;
        } else {
            return overdueDays * DEFAULT_FINE_RATE;
        }
    }
    
    public String generateBookReport(Book book) {
        StringBuilder report = new StringBuilder();
        
        report.append("=== BOOK REPORT ===\n");
        report.append("ISBN: ").append(book.getIsbn()).append("\n");
        report.append("Title: ").append(book.getTitle()).append("\n");
        report.append("Author: ").append(book.getAuthor()).append("\n");
        report.append("Category: ").append(book.getCategory()).append("\n");
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
     * Code Smell   : Magic Numbers
     * Fix          : Cleaned up the switch statement using "fall-through" cases.
     */
    public boolean isPopularCategory(String category) {
        switch (category) {
            case "Fiction":
            case "Non-Fiction":
            case "Children":
                return true; 
            case "Reference":
            default:
                return false;
        }
    }

}
