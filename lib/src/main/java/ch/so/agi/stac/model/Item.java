package ch.so.agi.stac.model;

public class Item {
    private Bbox bbox;

    public Bbox getBbox() {
        return bbox;
    }

    public void setBbox(Bbox bbox) {
        this.bbox = bbox;
    }
    
    // fluent api
    
    public Item bbox(Bbox bbox) {
        this.bbox = bbox;
        return this;
    }
}
