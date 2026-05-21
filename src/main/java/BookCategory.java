/**
 * Code Smell   : Primitive Obsession & Switch Statements
 * Fix          : Replaced String categories with an Enum to ensure type safety. 
 *                Stored fine rates and popularity status here to avoid if-else/switch chains.
 */
public enum BookCategory {
    REFERENCE(1.0, false),
    CHILDREN(0.25, true),
    FICTION(0.5, true),
    NON_FICTION(0.5, true),
    DEFAULT(0.5, false);

    private final double fineRate;
    private final boolean isPopular;

    BookCategory(double fineRate, boolean isPopular) {
        this.fineRate = fineRate;
        this.isPopular = isPopular;
    }

    public double getFineRate() { return fineRate; }
    public boolean isPopular() { return isPopular; }

    public static BookCategory fromString(String name) {
        if (name == null || name.trim().isEmpty()) {
            return DEFAULT;
        }
        try {
            return BookCategory.valueOf(name.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return DEFAULT;
        }
    }
}