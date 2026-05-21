import java.time.LocalDate;

/**
 * Code Smell   : Data Class
 * Fix          : Kept as a pure Data Object by design to fit the Service/Utility architecture.
 */
public class Book {
    private String isbn;
    private String title;
    private String author;
    
    /**
     * Code Smell   : Primitive Obsession
     * Fix          : Changed from String to BookCategory Enum.
     */
    private BookCategory category;
    
    private boolean isAvailable;
    private LocalDate dueDate;
    private String borrowerMemberId;
    
    public Book(String isbn, String title, String author, BookCategory category) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = true;
        this.dueDate = null;
        this.borrowerMemberId = null;
    }
    
    // === Getters and Setters ===
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public BookCategory getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public LocalDate getDueDate() { return dueDate; }
    public String getBorrowerMemberId() { return borrowerMemberId; }
    
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setBorrowerMemberId(String borrowerMemberId) { this.borrowerMemberId = borrowerMemberId; }
}