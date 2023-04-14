package ch.so.agi.stac.model;

public enum LinkMimeType {
    APPLICATION_JSON("application/json");
    
    private String value;

    LinkMimeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LinkMimeType fromString(String text) {
        for (LinkMimeType l : LinkMimeType.values()) {
            if (l.value.equalsIgnoreCase(text)) {
                return l;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
