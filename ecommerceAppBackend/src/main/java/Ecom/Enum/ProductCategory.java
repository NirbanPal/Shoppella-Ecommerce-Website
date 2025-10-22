package Ecom.Enum;

public enum ProductCategory {
    ELECTRONICS("Electronics"),
    FASHION("Fashion"),
    HOME_APPLIANCES("Home Appliances"),
    BEAUTY_AND_PERSONAL_CARE("Beauty & Personal Care"),
    SPORTS_AND_OUTDOORS("Sports & Outdoors"),
    BOOKS("Books"),
    TOYS_AND_GAMES("Toys & Games"),
    GROCERY("Grocery"),
    AUTOMOTIVE("Automotive"),
    HEALTH_AND_WELLNESS("Health & Wellness"),
    OFFICE_SUPPLIES("Office Supplies"),
    PET_SUPPLIES("Pet Supplies"),
    JEWELRY("Jewelry"),
    FOOTWEAR("Footwear"),
    MOBILE_AND_ACCESSORIES("Mobile & Accessories"),
    BABY_PRODUCTS("Baby Products");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Convert from string
    public static ProductCategory fromDisplayName(String displayName) {
        for (ProductCategory category : values()) {
            if (category.getDisplayName().equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + displayName);
    }
}
