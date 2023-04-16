package ch.so.agi.stac.model;

public enum RelType {
    SELF("self"),
    ROOT("root"),
    CHILD("child"),
    PARENT("parent"),
    ITEM("item"),
    COLLECTION("collection");
    
    private String value;

    RelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RelType fromString(String text) {
        for (RelType t : RelType.values()) {
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
