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

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
