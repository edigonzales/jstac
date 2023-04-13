package ch.so.agi.stac.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import ch.so.agi.stac.jackson.Interval;

public class Collection extends Catalog {
    
    public Collection() {
        super.type = CatalogType.COLLECTION;
        super.fileName = "collection.json";
    }
    
    @JsonProperty("extent")
    private Bbox bbox;
    
    @JsonProperty("temporal")
    private Interval interval;
    
    public Bbox getBbox() {
        return bbox;
    }

    public void setBbox(Bbox bbox) {
        this.bbox = bbox;
    }
        
    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }
    
    // fluent api

    public Collection bbox(Bbox bbox) {
        this.bbox = bbox;
        return this;
    }
    
    public Collection interval(Interval interval) {
        this.interval = interval;
        return this;
    }
}
