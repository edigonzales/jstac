package ch.so.agi.stac.model;

public enum CatalogType {
    COLLECTION("Collection"),
    CATALOG("Catalog");
    
    private String value;

    CatalogType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CatalogType fromString(String text) {
        for (CatalogType t : CatalogType.values()) {
            if (t.value.equalsIgnoreCase(text)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
