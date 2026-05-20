import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Member {
	
	/**
	 * Code Smell	: Magic Numbers
	 * Fix			: Replace Magic Number with Symbolic Constant
	 * Explanation	: Replaced magic numbers (hardcoded values) with named constants to improve readability.
	 */
	private static final int MAX_BORROWED_BOOKS = 5;
    private static final double MAX_FINES = 25.0;
    private static final int MAX_BORROW_COUNT = 50;
    
    private static final double RISK_PER_BORROWED = 0.1;
    private static final double RISK_PER_FINE_DOLLAR = 0.05;
    private static final double RISK_PER_BORROW = 0.02;
    private static final int RISK_MANY_BOOKS_THRESHOLD = 3;
    private static final double RISK_MANY_BOOKS = 1.5;
    private static final double RISK_HIGH_FINES_THRESHOLD = 10.0;
    private static final double RISK_HIGH_FINES = 2.0;
    private static final int NEW_MEMBER_THRESHOLD_DAYS = 30;
    private static final long MS_PER_DAY = 1000L * 60 * 60 * 24;
    private static final double RISK_NEW_MEMBER = 1.0;
    
    private String memberId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Date membershipDate;
    private List<String> borrowedBooks;
    private int borrowCount;
    private double totalFines;
    
    public Member(String memberId, String name, String email, String phoneNumber, String address) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.membershipDate = new Date();
        this.borrowedBooks = new ArrayList<>();
        this.borrowCount = 0;
        this.totalFines = 0.0;
    }
    
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public Date getMembershipDate() { return membershipDate; }
    public List<String> getBorrowedBooks() { return borrowedBooks; }
    public int getBorrowCount() { return borrowCount; }
    public double getTotalFines() { return totalFines; }
    
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setBorrowCount(int borrowCount) { this.borrowCount = borrowCount; }
    public void setTotalFines(double totalFines) { this.totalFines = totalFines; }
    
    public void addBorrowedBook(String isbn) {
        borrowedBooks.add(isbn);
        borrowCount++;
    }
    
    public void removeBorrowedBook(String isbn) {
        borrowedBooks.remove(isbn);
    }
    
    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", borrowedBooks=" + borrowedBooks.size() +
                '}';
    }
    
    /**
     * Code Smell	: Feature Envy (Couplers)
     * Fix			: Move Method
     * Explanation	: Moved the eligibility logic to Member class since it relies heavily on Member's internal data.
     */
    public boolean isEligibleForBorrowing(boolean checkHistory) {
        if (borrowedBooks.size() >= MAX_BORROWED_BOOKS) {
            return false;
        }
        
        if (totalFines > MAX_FINES) {
        	return false;
        }

        if (checkHistory && borrowCount > MAX_BORROW_COUNT) {
            return false;
        }

        return true;
    }
    
    /**
     * Code Smell	: Feature Envy (Couplers)
     * Fix			: Move Method
     * Explanation	: Moved the risk calculation logic to Member class since it relies heavily on Member's internal data.
     */
    public double calculateRisk() {
        double risk = 0.0;

        risk += borrowedBooks.size() * RISK_PER_BORROWED;
        risk += totalFines * RISK_PER_FINE_DOLLAR;
        risk += borrowCount * RISK_PER_BORROW;

        if (borrowedBooks.size() > RISK_MANY_BOOKS_THRESHOLD) {
            risk += RISK_MANY_BOOKS;
        }

        if (totalFines > RISK_HIGH_FINES_THRESHOLD) {
            risk += RISK_HIGH_FINES;
        }

        long daysSinceMembership = (new Date().getTime() - membershipDate.getTime()) / MS_PER_DAY;
        if (daysSinceMembership < NEW_MEMBER_THRESHOLD_DAYS) {
            risk += RISK_NEW_MEMBER;
        }

        return risk;
    }
}
