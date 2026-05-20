public class MemberValidator {
	
	/**
	 * Code Smell	: Magic Numbers
	 * Fix			: Replace Magic Number with Symbolic Constant
	 * Explanation	: Replaced magic numbers (hardcoded values) with named constants to improve readability.
	 */
	private static final int MIN_NAME_LENGTH = 2;
    private static final int MIN_PHONE_LENGTH = 10;
    private static final int MIN_ADDRESS_LENGTH = 5;
	
	/**
	 * Code Smell	: Long Parameter List (Bloaters) & Dead Code (Dispensables)
	 * Fix			: Remove Parameter & Delete Unused Code
	 * Explanation	: Removed unused parameters and dead logic to simplify the method.
	 */
    public boolean validateMemberForBorrowing(Member member, boolean checkHistory) {
        if (!isValidName(member.getName()) ||
        	!isValidEmail(member.getEmail()) ||
        	!isValidPhone(member.getPhoneNumber())) {
        	return false;
        }
        
        return member.isEligibleForBorrowing(checkHistory);
    }
    
    /**
	 * Code Smell	: Duplicate Code (Dispensables)
	 * Fix			: Extract Method
	 * Explanation	: Extracted duplicated string validation with private methods to eliminate repetition.
	 */
    public boolean validateMemberForRegistration(String name, String email, String phone, String address) {
        return isValidName(name) && 
        	   isValidEmail(email) &&
        	   isValidPhone(phone) &&
        	   isValidAddress(address);
    }
    
    private boolean isValidName(String name) {
    	return name != null && name.trim().length() >= MIN_NAME_LENGTH;
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    private boolean isValidPhone(String phone) {
        return phone != null && phone.length() >= MIN_PHONE_LENGTH;
    }
    
    private boolean isValidAddress(String address) {
        return address != null && address.trim().length() >= MIN_ADDRESS_LENGTH;
    }
}
